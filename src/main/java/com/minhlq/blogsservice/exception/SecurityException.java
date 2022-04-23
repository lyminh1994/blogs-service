package com.minhlq.blogsservice.exception;

/**
 * @author minhlq
 */
public class SecurityException extends RuntimeException {

  public SecurityException(String message) {
    super(message);
  }

  public SecurityException(String message, Exception exception) {
    super(message, exception);
  }
}
