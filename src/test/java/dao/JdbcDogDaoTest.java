package dao;

import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@ActiveProfiles("h2")
@ContextConfiguration("classpath:spring-web-servlet.xml")
public class JdbcDogDaoTest extends AbstractTestNGSpringContextTests {
    // TODO

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
        Dog dog = jdbcDogDao.createDog(randomDog().setName(unicode(100)));
        assertNotNull(dog.getId());
    }

    @Test
    public void appAcceptsSqlInjections() {
        Dog dog = jdbcDogDao.createDog(randomDog().setName("\"' blah"));
        assertNotNull(dog.getId());
    }

    private static Dog randomDog() {
        return new Dog().setName(unicode(6)).setWeight(integer(1, 100));
    }
}