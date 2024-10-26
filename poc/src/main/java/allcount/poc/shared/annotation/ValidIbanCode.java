package allcount.poc.shared.annotation;

import allcount.poc.shared.validator.IbanCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the IBAN code.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanCodeValidator.class)
public @interface ValidIbanCode {
    String INVALID_MESSAGE = "Invalid IBAN Code";

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
