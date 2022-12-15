package com.minhlq.blogsservice.annotation;

import com.minhlq.blogsservice.annotation.impl.validator.DuplicatedUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The custom validator to validate user username cannot duplicate.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicatedUsernameValidator.class)
public @interface DuplicatedUsernameConstraint {

    /**
     * Resolve a message in case of violation.
     *
     * @return the validation messages
     */
    String message() default "Username has existed";

    /**
     * Certain validation group that should be triggered.
     *
     * @return the class
     */
    Class<?>[] groups() default {};

    /**
     * Payload to be passed in this validation.
     *
     * @return the payload class
     */
    Class<? extends Payload>[] payload() default {};
}
