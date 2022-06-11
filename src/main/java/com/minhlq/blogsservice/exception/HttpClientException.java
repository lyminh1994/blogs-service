package com.minhlq.blogsservice.exception;

/**
 * Handles all http call exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public class HttpClientException extends RuntimeException {

  public HttpClientException(String message) {
    super(message);
  }

  public HttpClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
