package endpoint;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import dao.InMemoryDogDao;
import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.qala.datagen.RandomShortApi.*;
import static org.testng.Assert.*;

@WebAppConfiguration
@ContextConfiguration("classpath:spring-web-servlet.xml")
public class DogEndpointMockMvcTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private InMemoryDogDao dogDao;
    private MockMvcRequestSpecification request;

    @BeforeClass
    public void setUp() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        request = given().mockMvc(mockMvc).contentType(ContentType.JSON);
    }

    @Test
    public void getsDogById() {
        Dog expectedDog = dogDao.createDog(randomDog());
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
        Dog dog = dogDao.createDog(randomDog());
        request.delete("/dog/{id}", dog.getId()).then().statusCode(200);
        assertNull(dogDao.findById(dog.getId()));
    }

    @Test
    public void returns404_ifDogForDeletionIsNotFound() {
        request.delete("/dog/{id}", integer(900, 9000)).then().statusCode(404);
    }

    @Test
    public void updatesExistingDog() {
        Dog dog = randomDog();
        Dog updatedDog = request.body(dog).put("/dog/{id}", 1).then().statusCode(200).extract().body().as(Dog.class);
        assertEquals(1L, updatedDog.getId().longValue());
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
        return new Dog().setName(english(6)).setWeight(positiveDouble());
    }

    private static void assertDogsEquals(Dog actualDog, Dog expectedDog) {
        assertEquals(actualDog.getName(), expectedDog.getName());
        assertEquals(actualDog.getDate(), expectedDog.getDate());
        assertEquals(actualDog.getHeight(), expectedDog.getHeight());
        assertEquals(actualDog.getWeight(), expectedDog.getWeight());
    }
}