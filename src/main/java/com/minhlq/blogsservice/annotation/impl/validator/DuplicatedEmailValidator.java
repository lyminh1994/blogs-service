package com.minhlq.blogsservice.annotation.impl.validator;

import com.minhlq.blogsservice.annotation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * The implement for DuplicatedEmailValidator annotation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class DuplicatedEmailValidator
    implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  private final UserService userService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(value) && userService.isEmailExisted(value)) {
      context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("email", value);
      return false;
    }

    return true;
  }
}
