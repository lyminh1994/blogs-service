package com.minhlq.blogsservice.annotation.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.minhlq.blogsservice.annotation.validation.validator.DuplicatedArticleValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The custom validator to validate article name does not exist.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({METHOD, FIELD, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = DuplicatedArticleValidator.class)
public @interface DuplicatedArticleConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "Article name already existed";

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
