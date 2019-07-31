package endpoint;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import model.Dog;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.*;
import static io.qala.datagen.RandomShortApi.*;
import static org.testng.Assert.*;

public class DogEndpointRestAssuredTest {
    @Test
    public void getsDogById() {
        Dog expectedDog = requestWithBody(randomDog()).post("/dog").body().as(Dog.class);
        Dog dog = get("/dog/{id}", expectedDog.getId()).then().statusCode(200).extract().body().as(Dog.class);
        assertDogsEquals(dog, expectedDog);
    }

    @Test
    public void returns404_ifDogIsNotFound() {
        get("/dog/{id}", integer(1000, 2000)).then().statusCode(404);
    }

    @Test
    public void createsDog() {
        Dog dog = randomDog();
        Dog createdDog = requestWithBody(dog).post("/dog").then().statusCode(201).extract().body().as(Dog.class);
        assertDogsEquals(createdDog, dog);
    }

    @Test
    public void overridesIdWhenCreatesDog() {
        Dog dog = randomDog().setId(positiveLong());
        Dog actualDog = requestWithBody(dog).post("/dog").then().statusCode(201).extract().body().as(Dog.class);
        assertNotEquals(actualDog.getId(), dog.getId());
        assertDogsEquals(actualDog, dog);
    }

    @Test
    public void removesDog() {
        Dog dog = requestWithBody(randomDog()).post("/dog").then().statusCode(201).extract().body().as(Dog.class);
        delete("/dog/{id}", dog.getId()).then().statusCode(200);
        get("dog/{id}", dog.getId()).then().statusCode(404);
    }

    @Test
    public void returns404_ifDogForDeletionIsNotFound() {
        delete("/dog/{id}", integer(900, 9000)).then().statusCode(404);
    }

    @Test
    public void updatesExistingDog() {
        Dog dog = randomDog();
        Dog updatedDog = requestWithBody(dog).put("/dog/{id}", 1).then().statusCode(200).extract().body().as(Dog.class);
        assertEquals(1L, updatedDog.getId().longValue());
        assertDogsEquals(updatedDog, dog);
    }

    @Test
    public void createsNewDog_ifDogForUpdatingNotFound() {
        Dog dog = randomDog();
        int randomId = integer(1000, 2000);
        Dog actualDog = requestWithBody(dog).put("/dog/{id}", randomId).then().statusCode(200).extract().body().as(Dog.class);
        assertNotEquals(randomId, actualDog.getId());
        assertNotNull(actualDog.getId());
        assertDogsEquals(actualDog, dog);
    }

    private RequestSpecification requestWithBody(Dog dog) {
        return given().contentType(ContentType.JSON).body(dog);
    }

    private static Dog randomDog() {
        return new Dog().setName(english(6)).setWeight(positiveDouble());
    }

    private static void assertDogsEquals(Dog actualDog, Dog expectedDog) {
        assertEquals(actualDog.getName(), expectedDog.getName());
        assertEquals(actualDog.getDate(), expectedDog.getDate());
        assertEquals(actualDog.getHeight(), expectedDog.getHeight());
        assertEquals(actualDog.getWeight(), expectedDog.getWeight());
    }
}