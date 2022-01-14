package com.minhlq.blogsservice.validation.validator;

import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.validation.DuplicatedUsernameConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class DuplicatedUsernameValidator
    implements ConstraintValidator<DuplicatedUsernameConstraint, String> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isBlank(value) || userRepository.findByUsername(value).isEmpty();
  }
}