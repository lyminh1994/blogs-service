package com.minhlq.blogsservice.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

/**
 * Handles invalid request exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
public class InvalidRequestException extends RuntimeException {

  private final Errors errors;

  public InvalidRequestException(Errors errors) {
    super("");
    this.errors = errors;
  }
}
