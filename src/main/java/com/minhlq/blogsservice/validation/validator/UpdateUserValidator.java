package com.minhlq.blogsservice.validation.validator;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.validation.UpdateUserConstraint;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserValidator
    implements ConstraintValidator<UpdateUserConstraint, UpdateUserDto> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(UpdateUserDto value, ConstraintValidatorContext context) {
    String inputEmail = value.getParams().getEmail();
    final UserPrincipal targetUser = value.getTargetUser();

    boolean isEmailValid =
        userRepository
            .findByEmail(inputEmail)
            .map(user -> Objects.equals(user.getId(), targetUser.getId()))
            .orElse(true);
    if (!isEmailValid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("email already using by another user")
          .addPropertyNode("email")
          .addConstraintViolation();
    }

    return isEmailValid;
  }
}
