package com.minhlq.blogsservice.exception.handler;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonRootName("errors")
public class ErrorResource {

  private final List<FieldErrorResource> fieldErrors;
}
