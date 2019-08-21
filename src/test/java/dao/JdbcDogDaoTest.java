package dao;

import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@ContextConfiguration("classpath:spring-web-servlet.xml")
public class JdbcDogDaoTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private DogDao jdbcDogDao;

    @Test
    public void savesDog_ifNameLessThan100() {
        Dog dog = jdbcDogDao.createDog(randomDog());
        assertNotNull(dog.getId());
    }

    @Test
    public void throwsException_savingDog_ifNameSizeMoreThan100() {
        Dog dog = jdbcDogDao.createDog(randomDog().setName(english(101)));
        assertNull(dog.getId());
    }

    @Test
    public void savesDog_ifNameSizeIs100() {
        Dog dog = jdbcDogDao.createDog(randomDog());
        assertNotNull(dog.getId());
    }

    @Test
    public void appAcceptsSqlInjections() {
        Dog dog = jdbcDogDao.createDog(randomDog().setName("\"' blah"));
        assertNotNull(dog.getId());
    }

    private static Dog randomDog() {
        return new Dog().setName(english(6)).setWeight(positiveInteger());
    }
}