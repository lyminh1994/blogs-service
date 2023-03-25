package com.minhlq.blogsservice.annotation.validator;

import com.minhlq.blogsservice.annotation.DuplicatedUsernameConstraint;
import com.minhlq.blogsservice.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

/**
 * The implement for DuplicatedUsernameConstraint annotation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class DuplicatedUsernameValidator
    implements ConstraintValidator<DuplicatedUsernameConstraint, String> {

  private final UserService userService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(value) && userService.isUsernameExisted(value)) {
      ((ConstraintValidatorContextImpl) context).addMessageParameter("username", value);
      return false;
    }

    return true;
  }
}
