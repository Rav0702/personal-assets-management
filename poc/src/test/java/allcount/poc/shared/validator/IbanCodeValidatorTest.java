package allcount.poc.shared.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class IbanCodeValidatorTest {
    private IbanCodeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void beforeEach() {
        validator = new IbanCodeValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testNullValueReturnsFalse() {
        assertFalse(IbanCodeValidator.isValid(null));
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyValueReturnsFalse() {
        assertFalse(IbanCodeValidator.isValid(""));
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testInvalidValueReturnsFalse() {
        assertFalse(IbanCodeValidator.isValid("invalidIBAN"));
        assertFalse(validator.isValid("invalidIBAN", context));
    }

    @Test
    void testValidValueReturnsTrue() {
        assertTrue(IbanCodeValidator.isValid("IL170108000000012612345"));
        assertTrue(validator.isValid("IL170108000000012612345", context));

        assertTrue(IbanCodeValidator.isValid("XK051212012345678906"));
        assertTrue(validator.isValid("XK051212012345678906", context));

        assertTrue(IbanCodeValidator.isValid("PL10105000997603123456789123"));
        assertTrue(validator.isValid("PL10105000997603123456789123", context));
    }
}