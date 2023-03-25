package com.minhlq.blogsservice.annotation.validator;

import com.minhlq.blogsservice.annotation.UpdatePasswordConstraint;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.util.SecurityUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UpdatePasswordValidator
    implements ConstraintValidator<UpdatePasswordConstraint, String> {

  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    return passwordEncoder.matches(value, currentUser.getPassword());
  }
}
