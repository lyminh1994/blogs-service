package com.minhlq.blogsservice.exception.handler;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonRootName("errors")
@JsonSerialize(using = ErrorResourceSerializer.class)
public class ErrorResource {

  private final List<FieldErrorResource> fieldErrors;
}
