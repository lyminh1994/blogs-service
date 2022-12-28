package com.minhlq.blogsservice.exception.handler;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonRootName("errors")
@JsonSerialize(using = ErrorResourceSerializer.class)
public class ErrorResource {

  private final List<FieldErrorResource> fieldErrors;
}
