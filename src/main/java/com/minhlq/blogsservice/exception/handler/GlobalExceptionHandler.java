package com.minhlq.blogsservice.exception.handler;

import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A global exception handler for REST API.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ResourceNotFoundException.class})
  public String handlerResourceNotFound(ResourceNotFoundException ex) {
    return ex.getMessage();
  }
}
