package com.minhlq.blogsservice.exception.handler;

import com.minhlq.blogsservice.exception.HttpClientException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

  @ExceptionHandler(HttpClientException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Object handleHttpClientException(HttpClientException e) {
    Map<String, Object> result = new HashMap<>();
    result.put("code", e.getCode());
    result.put("message", e.getMessage());
    result.put("data", e.getData());

    return result;
  }
}
