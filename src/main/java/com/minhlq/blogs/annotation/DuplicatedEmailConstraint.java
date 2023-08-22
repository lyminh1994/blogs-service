package com.minhlq.blogs.annotation;

import com.minhlq.blogs.repository.UserRepository;
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
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * The custom validator to validate user email cannot duplicate.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicatedEmailConstraint.Validator.class)
public @interface DuplicatedEmailConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "{user.email.duplicated}";

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
  class Validator implements ConstraintValidator<DuplicatedEmailConstraint, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(DuplicatedEmailConstraint constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (StringUtils.isNotBlank(value) && userRepository.existsByEmail(value)) {
        var hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.addMessageParameter("email", value);
        return false;
      }

      return true;
    }
  }
}
