package com.minhlq.blogsservice.exception;

/**
 * Handles all Jwt exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public class SecurityException extends RuntimeException {

  public SecurityException(String message) {
    super(message);
  }

  public SecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
