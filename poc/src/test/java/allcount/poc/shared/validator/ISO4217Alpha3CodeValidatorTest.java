package allcount.poc.shared.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ISO4217Alpha3CodeValidatorTest {
    private ISO4217Alpha3CodeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void beforeEach() {
        validator = new ISO4217Alpha3CodeValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testNullValueReturnsFalse() {
        assertFalse(ISO4217Alpha3CodeValidator.isValid(null));
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyValueReturnsFalse() {
        assertFalse(ISO4217Alpha3CodeValidator.isValid(""));
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testInvalidValueReturnsFalse() {
        assertFalse(ISO4217Alpha3CodeValidator.isValid("123"));
        assertFalse(validator.isValid("123", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("AB2"));
        assertFalse(validator.isValid("AB2", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("abc"));
        assertFalse(validator.isValid("abc", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("PL"));
        assertFalse(validator.isValid("PL", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("PLNS"));
        assertFalse(validator.isValid("PLNS", context));
    }

    @Test
    void testLowercaseValueReturnsFalse() {
        assertFalse(ISO4217Alpha3CodeValidator.isValid("pln"));
        assertFalse(validator.isValid("pln", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("usd"));
        assertFalse(validator.isValid("usd", context));

        assertFalse(ISO4217Alpha3CodeValidator.isValid("eur"));
        assertFalse(validator.isValid("eur", context));
    }

    @Test
    void testValidValueReturnsTrue() {
        assertTrue(ISO4217Alpha3CodeValidator.isValid("PLN"));
        assertTrue(validator.isValid("PLN", context));

        assertTrue(ISO4217Alpha3CodeValidator.isValid("USD"));
        assertTrue(validator.isValid("USD", context));

        assertTrue(ISO4217Alpha3CodeValidator.isValid("EUR"));
        assertTrue(validator.isValid("EUR", context));
    }
}