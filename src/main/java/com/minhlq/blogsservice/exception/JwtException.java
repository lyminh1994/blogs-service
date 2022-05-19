package com.minhlq.blogsservice.exception;

/**
 * Handles all Jwt exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public class JwtException extends RuntimeException {

  public JwtException(String message) {
    super(message);
  }

  public JwtException(String message, Exception exception) {
    super(message, exception);
  }
}
