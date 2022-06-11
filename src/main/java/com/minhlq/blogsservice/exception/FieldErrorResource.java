package com.minhlq.blogsservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Field error response.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {

  private final String resource;

  private final String field;

  private final String code;

  private final String message;
}
