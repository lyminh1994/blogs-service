package com.minhlq.blogsservice.annotation.impl.validator;

import com.minhlq.blogsservice.annotation.UpdateUserConstraint;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

/**
 * The implement for UpdateUserConstraint annotation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class UpdateUserValidator
    implements ConstraintValidator<UpdateUserConstraint, UpdateUserDto> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(UpdateUserDto value, ConstraintValidatorContext context) {
    final String email = value.params().email();
    final String phone = value.params().phone();
    final UserPrincipal targetUser = value.targetUser();

    boolean isValidEmail =
        userRepository
            .findByEmail(email)
            .map(user -> Objects.equals(user.getId(), targetUser.id()))
            .orElse(true);
    if (!isValidEmail) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("email already using by another user")
          .addPropertyNode("email")
          .addConstraintViolation();
    }

    boolean isValidPhone =
        userRepository
            .findByPhone(phone)
            .map(user -> Objects.equals(user.getId(), targetUser.id()))
            .orElse(true);
    if (!isValidPhone) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("phone number already using by another user")
          .addPropertyNode("phone")
          .addConstraintViolation();
    }

    return isValidEmail && isValidPhone;
  }
}
