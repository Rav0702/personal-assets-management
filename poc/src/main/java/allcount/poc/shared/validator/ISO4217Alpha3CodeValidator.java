package allcount.poc.shared.validator;

import allcount.poc.shared.annotation.ValidISO4217Alpha3Code;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISO4217Alpha3CodeValidator implements ConstraintValidator<ValidISO4217Alpha3Code, String> {
    private static final String ISO4217_PATTERN = "^[A-Z]{3}$";

    @Override
    public void initialize(ValidISO4217Alpha3Code constraintAnnotation) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(string);
    }

    public static boolean isValid(String string) {
        return string != null && string.matches(ISO4217_PATTERN);
    }
}
