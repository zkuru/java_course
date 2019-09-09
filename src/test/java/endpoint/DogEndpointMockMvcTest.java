package endpoint;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.DogService;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.qala.datagen.RandomShortApi.*;
import static org.testng.Assert.*;

@ComponentTest
public class DogEndpointMockMvcTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DogService transactionalDogService;
    private MockMvcRequestSpecification request;

    @BeforeClass
    public void setUp() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        request = given().mockMvc(mockMvc).contentType(ContentType.JSON);
    }

    @Test
    public void getsDogById() {
        Dog expectedDog = transactionalDogService.createDog(randomDog());
        Dog dog = request.get("/dog/{id}", expectedDog.getId()).then().statusCode(200)
                .extract().body().as(Dog.class);
        assertDogsEquals(dog, expectedDog);
    }

    @Test
    public void returns404_ifDogIsNotFound() {
        request.get("/dog/{id}", integer(1000, 2000)).then().statusCode(404);
    }

    @Test
    public void createsDog() {
        Dog dog = randomDog();
        Dog createdDog = request.body(dog).post("/dog").then().statusCode(201).extract().body().as(Dog.class);
        assertDogsEquals(createdDog, dog);
    }

    @Test
    public void overridesIdWhenCreatesDog() {
        Dog dog = randomDog().setId(positiveLong());
        Dog actualDog = request.body(dog).post("/dog").then().statusCode(201).extract().body().as(Dog.class);
        assertNotEquals(actualDog.getId(), dog.getId());
        assertDogsEquals(actualDog, dog);
    }

    @Test
    public void removesDog() {
        Dog dog = transactionalDogService.createDog(randomDog());
        request.delete("/dog/{id}", dog.getId()).then().statusCode(200);
        assertNull(transactionalDogService.findById(dog.getId()));
    }

    @Test
    public void returns404_ifDogForDeletionIsNotFound() {
        request.delete("/dog/{id}", integer(900, 9000)).then().statusCode(404);
    }

    @Test
    public void updatesExistingDog() {
        Dog expectedDog = transactionalDogService.createDog(randomDog());
        Dog dog = randomDog();
        Dog updatedDog = request.body(dog).put("/dog/{id}", expectedDog.getId()).then().statusCode(200).extract().body().as(Dog.class);
        assertEquals(updatedDog.getId(), expectedDog.getId());
        assertDogsEquals(updatedDog, dog);
    }

    @Test
    public void createsNewDog_ifDogForUpdatingNotFound() {
        Dog dog = randomDog();
        int randomId = integer(1000, 2000);
        Dog actualDog = request.body(dog).put("/dog/{id}", randomId).then().statusCode(200).extract().body().as(Dog.class);
        assertNotEquals(randomId, actualDog.getId());
        assertNotNull(actualDog.getId());
        assertDogsEquals(actualDog, dog);
    }

    @Test
    public void validationFailsWhileCreatingDog_ifNameIsNull() {
        Dog dog = new Dog();
        request.body(dog).post("/dog").then().statusCode(400);
    }

    private static Dog randomDog() {
        return new Dog().setName(english(6)).setWeight(integer(0, 100));
    }

    private static void assertDogsEquals(Dog actualDog, Dog expectedDog) {
        assertEquals(actualDog.getName(), expectedDog.getName());
        assertEquals(actualDog.getDate(), expectedDog.getDate());
        assertEquals(actualDog.getHeight(), expectedDog.getHeight());
        assertEquals(actualDog.getWeight(), expectedDog.getWeight(), 0.0001);
    }
}