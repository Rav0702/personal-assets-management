package allcount.poc.shared.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class IBANCodeValidatorTest {
    private IBANCodeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void beforeEach() {
        validator = new IBANCodeValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testNullValueReturnsFalse() {
        assertFalse(IBANCodeValidator.isValid(null));
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyValueReturnsFalse() {
        assertFalse(IBANCodeValidator.isValid(""));
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testInvalidValueReturnsFalse() {
        assertFalse(IBANCodeValidator.isValid("invalidIBAN"));
        assertFalse(validator.isValid("invalidIBAN", context));
    }

    @Test
    void testValidValueReturnsTrue() {
        assertTrue(IBANCodeValidator.isValid("IL170108000000012612345"));
        assertTrue(validator.isValid("IL170108000000012612345", context));

        assertTrue(IBANCodeValidator.isValid("XK051212012345678906"));
        assertTrue(validator.isValid("XK051212012345678906", context));

        assertTrue(IBANCodeValidator.isValid("PL10105000997603123456789123"));
        assertTrue(validator.isValid("PL10105000997603123456789123", context));
    }
}