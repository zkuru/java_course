package endpoint;

import com.jayway.restassured.http.ContentType;
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
import service.DogService;

import java.util.Date;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.testng.Assert.*;

@WebAppConfiguration
@ContextConfiguration("classpath:test-context.xml")
public class DogEndpointTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DogService dogService;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getsDogById() {
        given().mockMvc(mockMvc).when().get("/dog/1").then().statusCode(200)
                .body(containsString("bobik"));
    }

    @Test
    public void returns404_ifDogIsNotFound() {
        given().mockMvc(mockMvc).when().get("/dog/999").then().statusCode(404);
    }

    @Test
    public void createsDog() {
        Dog dog = new Dog().setName("Name").setWeight(23.5).setDate(new Date());
        Dog actualDog = given().mockMvc(mockMvc).contentType(ContentType.JSON).body(dog)
                .when().post("/dog").then().statusCode(200).extract().body().as(Dog.class);
        assertNotNull(actualDog.getId());
        assertEquals(dog.getName(), actualDog.getName());
    }

    @Test
    public void overridesIdWhenCreatesDog() {
        Dog dog = new Dog().setId(1L).setName("Name").setWeight(23.5).setDate(new Date());
        Dog actualDog = given().mockMvc(mockMvc).contentType(ContentType.JSON).body(dog)
                .when().post("/dog").then().statusCode(200).extract().body().as(Dog.class);
        assertNotEquals(dog.getId(), actualDog.getId());
    }

    @Test
    public void removesDog() {
        given().mockMvc(mockMvc).when().delete("/dog/2").then().statusCode(200);
        assertNull(dogService.findById(2L));
    }

    @Test
    public void returns404_ifDogForDeletionIsNotFound() {
        given().mockMvc(mockMvc).when().delete("/dog/111").then().statusCode(404);
    }

    @Test
    public void updatesExistingDog() {
        Dog dog = new Dog().setName("Name").setWeight(23.5).setDate(new Date());
        Dog actualDog = given().mockMvc(mockMvc).contentType(ContentType.JSON).body(dog)
                .when().put("/dog/1").then().statusCode(200).extract().body().as(Dog.class);
        assertEquals(1L, actualDog.getId().longValue());
    }

    @Test
    public void createsNewDogForUpdatingNotPound() {
        Dog dog = new Dog().setName("Name").setWeight(23.5).setDate(new Date());
        Dog actualDog = given().mockMvc(mockMvc).contentType(ContentType.JSON).body(dog)
                .when().put("/dog/111").then().statusCode(200).extract().body().as(Dog.class);
        assertNotEquals(111L, actualDog.getId());
        assertNotNull(actualDog.getId());
    }
}