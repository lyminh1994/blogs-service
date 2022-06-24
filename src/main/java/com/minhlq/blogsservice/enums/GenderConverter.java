package com.minhlq.blogsservice.enums;

import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
        .filter(c -> c.getCode().equals(code))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
