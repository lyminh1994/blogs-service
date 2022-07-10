package com.minhlq.blogsservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Handles all http call exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HttpClientException extends RuntimeException {

  private final int code;

  private final String message;

  private Object data;

  public HttpClientException(HttpStatus status) {
    super(status.getReasonPhrase());
    this.code = status.value();
    this.message = status.getReasonPhrase();
  }

  public HttpClientException(HttpStatus status, Object data) {
    this(status);
    this.data = data;
  }

  public HttpClientException(Integer code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public HttpClientException(Integer code, String message, Object data) {
    this(code, message);
    this.data = data;
  }
}
