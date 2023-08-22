package com.minhlq.blogs.annotation;

import com.minhlq.blogs.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdatePasswordConstraint.Validator.class)
public @interface UpdatePasswordConstraint {

  /**
   * Resolve a message in case of violation.
   *
   * @return the validation messages
   */
  String message() default "{user.current.password.invalid}";

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
  class Validator implements ConstraintValidator<UpdatePasswordConstraint, String> {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void initialize(UpdatePasswordConstraint constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      var currentUser = userService.getCurrentUser();
      return passwordEncoder.matches(value, currentUser.getPassword());
    }
  }
}
