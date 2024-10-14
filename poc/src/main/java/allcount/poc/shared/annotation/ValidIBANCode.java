package allcount.poc.shared.annotation;

import allcount.poc.shared.validator.IBANCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IBANCodeValidator.class)
public @interface ValidIBANCode {
    String INVALID_MESSAGE = "Invalid IBAN Code";

    String message() default INVALID_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
