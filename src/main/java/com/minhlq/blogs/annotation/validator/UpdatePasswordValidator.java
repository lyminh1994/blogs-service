package com.minhlq.blogs.annotation.validator;

import com.minhlq.blogs.annotation.UpdatePasswordConstraint;
import com.minhlq.blogs.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UpdatePasswordValidator
    implements ConstraintValidator<UpdatePasswordConstraint, String> {

  private final PasswordEncoder passwordEncoder;

  private final UserService userService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    var currentUser = userService.getCurrentUser();
    return passwordEncoder.matches(value, currentUser.getPassword());
  }
}
