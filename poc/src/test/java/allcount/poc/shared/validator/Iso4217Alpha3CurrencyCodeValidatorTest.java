package allcount.poc.shared.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class Iso4217Alpha3CurrencyCodeValidatorTest {
    private Iso4217Alpha3CurrencyCodeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void beforeEach() {
        validator = new Iso4217Alpha3CurrencyCodeValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testNullValueReturnsFalse() {
        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid(null));
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyValueReturnsFalse() {
        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid(""));
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testInvalidValueReturnsFalse() {
        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("123"));
        assertFalse(validator.isValid("123", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("AB2"));
        assertFalse(validator.isValid("AB2", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("abc"));
        assertFalse(validator.isValid("abc", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("PL"));
        assertFalse(validator.isValid("PL", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("PLNS"));
        assertFalse(validator.isValid("PLNS", context));
    }

    @Test
    void testLowercaseValueReturnsFalse() {
        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("pln"));
        assertFalse(validator.isValid("pln", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("usd"));
        assertFalse(validator.isValid("usd", context));

        assertFalse(Iso4217Alpha3CurrencyCodeValidator.isValid("eur"));
        assertFalse(validator.isValid("eur", context));
    }

    @Test
    void testValidValueReturnsTrue() {
        assertTrue(Iso4217Alpha3CurrencyCodeValidator.isValid("PLN"));
        assertTrue(validator.isValid("PLN", context));

        assertTrue(Iso4217Alpha3CurrencyCodeValidator.isValid("USD"));
        assertTrue(validator.isValid("USD", context));

        assertTrue(Iso4217Alpha3CurrencyCodeValidator.isValid("EUR"));
        assertTrue(validator.isValid("EUR", context));
    }
}