package allcount.poc.shared.validator;

import allcount.poc.shared.annotation.ValidIBANCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IBANCodeValidator implements ConstraintValidator<ValidIBANCode, String> {
    private static final String IBAN_PATTERN = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$";

    @Override
    public void initialize(ValidIBANCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(string);
    }

    public static boolean isValid(String string) {
        return string != null && string.matches(IBAN_PATTERN);
    }
}
