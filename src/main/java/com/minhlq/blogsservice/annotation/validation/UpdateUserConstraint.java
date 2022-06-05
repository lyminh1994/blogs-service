package com.minhlq.blogsservice.annotation.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.minhlq.blogsservice.annotation.validation.validator.UpdateUserValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The custom validator to validate user email has used by another user.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({
  TYPE, FIELD, METHOD, PARAMETER,
})
@Retention(RUNTIME)
@Constraint(validatedBy = UpdateUserValidator.class)
public @interface UpdateUserConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "invalid update user params";

  /**
   * Certain validation group that should be triggered.
   *
   * @return the class
   */
  Class[] groups() default {};

  /**
   * Payload to be passed in this validation.
   *
   * @return the payload class
   */
  Class<? extends Payload>[] payload() default {};
}
