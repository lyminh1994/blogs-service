package com.minhlq.blogsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles all Jwt exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "exception.message")
public class SecurityException extends RuntimeException {

  public SecurityException(String message) {
    super(message);
  }

  public SecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
