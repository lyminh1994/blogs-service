package com.minhlq.blogsservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles unauthorized exceptions for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAuthorizationException extends RuntimeException {}
