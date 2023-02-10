package com.minhlq.blogsservice.annotation.impl.validator;

import com.minhlq.blogsservice.annotation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isBlank(value) || userRepository.findByEmail(value).isEmpty();
  }
}
