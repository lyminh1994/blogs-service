package com.minhlq.blogsservice.exception;

public class SecurityException extends RuntimeException {

  public SecurityException(String message) {
    super(message);
  }

  public SecurityException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
