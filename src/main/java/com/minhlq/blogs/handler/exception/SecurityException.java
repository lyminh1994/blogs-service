package com.minhlq.blogs.handler.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Handles all Jwt exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public class SecurityException extends AuthenticationException {

  public SecurityException(String message) {
    super(message);
  }
}
