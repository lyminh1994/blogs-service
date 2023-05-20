package com.minhlq.blogs.annotation;

import com.minhlq.blogs.annotation.validator.DuplicatedArticleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The custom validator to validate article name does not exist.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicatedArticleValidator.class)
public @interface DuplicatedArticleConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "{article.slug.duplicated}";

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
