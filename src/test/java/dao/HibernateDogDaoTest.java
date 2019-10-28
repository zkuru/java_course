package dao;

import endpoint.ComponentTest;
import model.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

import static io.qala.datagen.RandomShortApi.*;
import static org.testng.Assert.*;

@ComponentTest
public class HibernateDogDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private DogDao hibernateDogDao;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void deletesDog() {
        Dog dog = hibernateDogDao.createDog(randomDog());
        hibernateDogDao.deleteDog(dog.getId());
        flushAndClear();
        assertNull(hibernateDogDao.findById(dog.getId()));
    }

    @Test
    public void updatesDog() {
        Dog dog = hibernateDogDao.createDog(randomDog());
        flushAndClear();
        Dog updatedDog = hibernateDogDao.updateDog(randomDog().setId(dog.getId()));
        flushAndClear();
        assertDogsEquals(updatedDog, hibernateDogDao.findById(dog.getId()));
    }

    @Test
    public void createsNewDogIfIdIsNotSpecified() {
        Dog updatedDog = hibernateDogDao.updateDog(randomDog());
        flushAndClear();
        assertNotNull(updatedDog.getId());
    }

    @Test
    public void savesDog_ifNameIs100() {
        Dog dog = hibernateDogDao.createDog(randomDog().setName(unicode(100)));
        flushAndClear();
        assertNotNull(dog.getId());
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void throwsException_savingDog_ifNameSizeMoreThan100() {
        hibernateDogDao.createDog(randomDog().setName(english(101)));
    }

    @Test
    public void savingDog_sqlInjectionsAreIgnored() {
        Dog dog = hibernateDogDao.createDog(randomDog().setName("\"' blah"));
        flushAndClear();
        assertNotNull(dog.getId());
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private void flushAndClear() {
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    private static Dog randomDog() {
        return new Dog().setName(unicode(6)).setWeight(integer(1, 100));
    }

    private static void assertDogsEquals(Dog actualDog, Dog expectedDog) {
        assertEquals(actualDog.getId(), expectedDog.getId());
        assertEquals(actualDog.getName(), expectedDog.getName());
        assertEquals(actualDog.getDate(), expectedDog.getDate());
        assertEquals(actualDog.getHeight(), expectedDog.getHeight());
        assertEquals(actualDog.getWeight(), expectedDog.getWeight(), 0.0001);
    }
}