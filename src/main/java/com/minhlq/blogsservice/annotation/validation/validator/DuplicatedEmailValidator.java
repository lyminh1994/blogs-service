package com.minhlq.blogsservice.annotation.validation.validator;

import com.minhlq.blogsservice.annotation.validation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.repository.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class DuplicatedEmailValidator
    implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isBlank(value) || userRepository.findByEmail(value).isEmpty();
  }
}
