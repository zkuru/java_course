package model;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.Double;
import static io.qala.datagen.RandomShortApi.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class DogTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validationFails_ifNameIsNull() {
        Dog dog = new Dog();
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "must not be null";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationFails_ifNameExceedsMaxSize() {
        Dog dog = new Dog().setName(english(110));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "size must be between 0 and 100";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifNameSizeIs100() {
        Dog dog = new Dog().setName(english(100));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationPasses_ifNameLessThen100() {
        Dog dog = new Dog().setName(english(10));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationFails_ifDateIsAfterNow() {
        long today = new Date().getTime();
        Dog dog = new Dog().setName(english(3)).setDate(new Date(today + 1000000));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "must be a past date";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifDateIsNull() {
        Dog dog = new Dog().setName(english(3));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationPasses_ifDateIsBeforeNow() {
        Dog dog = new Dog().setName(english(3)).setDate(new Date(1));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationFails_ifHeightIsNegative() {
        Dog dog = new Dog().setName(english(3)).setHeight(integer(-100, -1));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);

        String message = "must be greater than 0";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifHeightIsPositive() {
        Dog dog = new Dog().setName(english(3)).setHeight(positiveInteger());
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationFails_ifWeightIsNegative() {
        Dog dog = new Dog().setName(english(3)).setWeight(Double(-100, -1));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "must be greater than 0";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifWeightIsPositive() {
        Dog dog = new Dog().setName(english(3)).setWeight(positiveDouble());
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    private static void assertHasMessage(Set<ConstraintViolation<Dog>> violations, String message) {
        assertTrue(violations.stream().map(ConstraintViolation::getMessage).anyMatch(message::equals));
    }
}