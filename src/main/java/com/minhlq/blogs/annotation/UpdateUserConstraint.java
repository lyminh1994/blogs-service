package com.minhlq.blogs.annotation;

import com.minhlq.blogs.dto.UpdateUserDto;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * The custom validator to validate user email has used by another user.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateUserConstraint.Validator.class)
public @interface UpdateUserConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "";

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

  @RequiredArgsConstructor
  class Validator implements ConstraintValidator<UpdateUserConstraint, UpdateUserDto> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UpdateUserConstraint constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateUserDto value, ConstraintValidatorContext context) {
      final var email = value.params().email();
      final var phone = value.params().phone();
      final var targetUser = value.targetUser();

      var isValidEmail =
          StringUtils.isBlank(email)
              || userRepository
                  .findByEmail(email)
                  .map(user -> Objects.equals(user.getId(), targetUser.getId()))
                  .orElse(true);

      var hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
      if (!isValidEmail) {
        hibernateContext.disableDefaultConstraintViolation();
        hibernateContext.addMessageParameter("email", email);
        hibernateContext
            .buildConstraintViolationWithTemplate("{user.email.duplicated}")
            .addPropertyNode("email")
            .addConstraintViolation();
      }

      var isValidPhone =
          StringUtils.isBlank(phone)
              || userRepository
                  .findByPhone(phone)
                  .map(user -> Objects.equals(user.getId(), targetUser.getId()))
                  .orElse(true);
      if (!isValidPhone) {
        hibernateContext.disableDefaultConstraintViolation();
        hibernateContext.addMessageParameter("phone", phone);
        hibernateContext
            .buildConstraintViolationWithTemplate("{user.phone.duplicated}")
            .addPropertyNode("phone")
            .addConstraintViolation();
      }

      return isValidEmail && isValidPhone;
    }
  }
}
