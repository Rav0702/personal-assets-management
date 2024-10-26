package allcount.poc.shared.annotation;

import allcount.poc.shared.validator.Iso4217Alpha3CurrencyCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating ISO-4217 Alpha-3 Code.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Iso4217Alpha3CurrencyCodeValidator.class)
public @interface ValidCurrencyCode {
    String INVALID_MESSAGE = "Invalid ISO-4217 Alpha-3 Code";

    /**
     * Message.
     *
     * @return the message
     */
    String message() default INVALID_MESSAGE;

    /**
     * Groups.
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}
