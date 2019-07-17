package model;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class DogTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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
        Dog dog = new Dog().setName("AaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaaAaaaaaaaa");
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "size must be between 0 and 100";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationFails_ifDateIsAfterNow() {
        long today = new Date().getTime();
        Dog dog = new Dog().setName("asd").setDate(new Date(today + 1000000));
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "must be a past date";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifDateIsNull() {
        Dog dog = new Dog().setName("asd");
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationPasses_ifNameLessThen100() {
        Dog dog = new Dog().setName("asd");
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void validationFails_ifHeightIsNegative() {
        Dog dog = new Dog().setName("asd").setHeight(0);
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);

        String message = "must be greater than 0";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationFails_ifWeightIsNegative() {
        Dog dog = new Dog().setName("asd").setWeight(-2.4);
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        String message = "must be greater than 0";
        assertEquals(1, constraintViolations.size());
        assertHasMessage(constraintViolations, message);
    }

    @Test
    public void validationPasses_ifAllFieldsValid() {
        Dog dog = new Dog().setName("asd").setDate(new Date(1000)).setHeight(1).setWeight(3.3);
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(0, constraintViolations.size());
    }

    private static void assertHasMessage(Set<ConstraintViolation<Dog>> violations, String message) {
        assertTrue(violations.stream().map(ConstraintViolation::getMessage).anyMatch(message::equals));
    }
}