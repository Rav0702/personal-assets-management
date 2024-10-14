package allcount.poc.shared.annotation;

import allcount.poc.shared.validator.ISO4217Alpha3CodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ISO4217Alpha3CodeValidator.class)
public @interface ValidISO4217Alpha3Code {
    String INVALID_MESSAGE = "Invalid ISO-4217 Alpha-3 Code";

    String message() default INVALID_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
