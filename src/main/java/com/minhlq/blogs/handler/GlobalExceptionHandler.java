package com.minhlq.blogs.handler;

import com.minhlq.blogs.dto.ErrorsResource;
import com.minhlq.blogs.dto.FieldErrorResource;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A global exception handler for REST API.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    var fieldErrorResources =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest().body(new ErrorsResource(fieldErrorResources));
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    var fieldErrorResources =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    new FieldErrorResource(
                        violation.getRootBeanClass().getName(),
                        getParam(violation.getPropertyPath().toString()),
                        violation
                            .getConstraintDescriptor()
                            .getAnnotation()
                            .annotationType()
                            .getSimpleName(),
                        violation.getMessage()))
            .toList();

    return ResponseEntity.badRequest().body(new ErrorsResource(fieldErrorResources));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    FieldErrorResource errorResource =
        new FieldErrorResource(
            null,
            null,
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            StringUtils.defaultString(ex.getMessage(), ex.getLocalizedMessage()));
    return ResponseEntity.badRequest()
        .body(new ErrorsResource(Collections.singletonList(errorResource)));
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  public ErrorsResource handleResourceNotFound(ResourceNotFoundException ex, Locale locale) {
    FieldErrorResource errorResource =
        new FieldErrorResource(
            null,
            null,
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            StringUtils.defaultString(
                ex.getMessage(), messageSource.getMessage("not.found", null, locale)));
    return new ErrorsResource(Collections.singletonList(errorResource));
  }

  private String getParam(String s) {
    var splits = s.split("\\.");
    if (splits.length == 1) {
      return s;
    }

    return String.join(".", Arrays.copyOfRange(splits, 2, splits.length));
  }
}
