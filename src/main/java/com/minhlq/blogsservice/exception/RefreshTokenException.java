package com.minhlq.blogsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenException extends RuntimeException {

  public RefreshTokenException(String refreshToken, String message) {
    super(String.format("Failed for [%s]: %s", refreshToken, message));
  }
}
