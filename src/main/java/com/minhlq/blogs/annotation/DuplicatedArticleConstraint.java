package com.minhlq.blogs.annotation;

import com.minhlq.blogs.repository.ArticleRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
@Constraint(validatedBy = DuplicatedArticleConstraint.Validator.class)
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

  @RequiredArgsConstructor
  class Validator implements ConstraintValidator<DuplicatedArticleConstraint, String> {

    private final ArticleRepository articleRepository;

    @Override
    public void initialize(DuplicatedArticleConstraint constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      return StringUtils.isBlank(value) || !articleRepository.existsBySlug(value);
    }
  }
}
