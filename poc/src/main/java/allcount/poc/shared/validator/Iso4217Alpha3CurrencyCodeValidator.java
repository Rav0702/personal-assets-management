package allcount.poc.shared.validator;

import allcount.poc.shared.annotation.ValidCurrencyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Iso4217Alpha3CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    private static final String ISO4217_PATTERN = "^[A-Z]{3}$";

    @Override
    public void initialize(ValidCurrencyCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(string);
    }

    public static boolean isValid(String string) {
        return string != null && string.matches(ISO4217_PATTERN);
    }
}
