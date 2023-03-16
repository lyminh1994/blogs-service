package com.minhlq.blogsservice.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Gender gender) {
    if (gender == null) {
      return null;
    }

    return gender.getCode();
  }

  @Override
  public Gender convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(Gender.values())
        .filter(gender -> gender.getCode().equals(code))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
