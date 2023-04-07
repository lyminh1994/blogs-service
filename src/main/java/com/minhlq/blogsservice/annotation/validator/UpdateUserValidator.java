package com.minhlq.blogsservice.annotation.validator;

import com.minhlq.blogsservice.annotation.UpdateUserConstraint;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

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
    final var email = value.params().email();
    final var phone = value.params().phone();
    final var targetUser = value.targetUser();

    var isValidEmail =
        StringUtils.isBlank(email)
            || userRepository
                .findByEmail(email)
                .map(user -> Objects.equals(user.getId(), targetUser.id()))
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
                .map(user -> Objects.equals(user.getId(), targetUser.id()))
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
