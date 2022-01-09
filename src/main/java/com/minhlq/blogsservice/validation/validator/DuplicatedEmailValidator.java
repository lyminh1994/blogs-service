package com.minhlq.blogsservice.validation.validator;

import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.validation.DuplicatedEmailConstraint;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicatedEmailValidator implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isBlank(value) || userRepository.findByEmail(value).isEmpty();
  }

}
