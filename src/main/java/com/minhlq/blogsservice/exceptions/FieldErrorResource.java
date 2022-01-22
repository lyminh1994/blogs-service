package com.minhlq.blogsservice.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {

  private final String resource;

  private final String field;

  private final String code;

  private final String message;
}
