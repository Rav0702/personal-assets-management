package allcount.poc.shared.validator;

import allcount.poc.shared.annotation.ValidCurrencyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the ISO-4217 Alpha-3 code.
 */
public class Iso4217Alpha3CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    private static final String ISO4217_PATTERN = "^[A-Z]{3}$";

    /**
     * Checks if the string is a valid ISO-4217 Alpha-3 code.
     *
     * @param string the string
     * @return true if the string is a valid ISO-4217 Alpha-3 code, false otherwise
     */
    public static boolean isValid(String string) {
        return string != null && string.matches(ISO4217_PATTERN);
    }

    /**
     * Checks if the string is a valid ISO-4217 Alpha-3 code.
     *
     * @param string                     the string
     * @param constraintValidatorContext the constraint validator context
     * @return true if the string is a valid ISO-4217 Alpha-3 code, false otherwise
     */
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(string);
    }

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation the constraint annotation
     */
    @Override
    public void initialize(ValidCurrencyCode constraintAnnotation) {
    }
}
