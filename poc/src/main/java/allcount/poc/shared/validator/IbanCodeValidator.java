package allcount.poc.shared.validator;

import allcount.poc.shared.annotation.ValidIbanCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the IBAN code.
 */
public class IbanCodeValidator implements ConstraintValidator<ValidIbanCode, String> {
    private static final String IBAN_PATTERN = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$";

    /**
     * Checks if the string is a valid IBAN code.
     *
     * @param string the string
     * @return true if the string is a valid IBAN code, false otherwise
     */
    public static boolean isValid(String string) {
        return string == null || string.matches(IBAN_PATTERN);
    }

    /**
     * Checks if the string is a valid IBAN code.
     *
     * @param string                     the string
     * @param constraintValidatorContext the constraint validator context
     * @return true if the string is a valid IBAN code, false otherwise
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
    public void initialize(ValidIbanCode constraintAnnotation) {
    }
}
