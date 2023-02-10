package com.minhlq.blogsservice.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorResource {

  private final String resource;

  private final String field;

  private final String code;

  private final String message;
}
