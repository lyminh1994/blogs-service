package com.minhlq.blogsservice.exception.handler;

import com.minhlq.blogsservice.exception.ErrorResource;
import com.minhlq.blogsservice.exception.FieldErrorResource;
import com.minhlq.blogsservice.exception.InvalidRequestException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({InvalidRequestException.class})
  public ResponseEntity<Object> handleInvalidRequest(RuntimeException ex, WebRequest request) {
    InvalidRequestException invalidRequestException = (InvalidRequestException) ex;
    List<FieldErrorResource> errorResources =
        invalidRequestException.getErrors().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    ErrorResource error = new ErrorResource(errorResources);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Map<String, String> handleInvalidAuthentication(BadCredentialsException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("message", "invalid username or password!");

    return body;
  }

  @Override
  @NonNull
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request) {
    List<FieldErrorResource> errorResources =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

    return ResponseEntity.badRequest().body(new ErrorResource(errorResources));
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResource handleConstraintViolation(ConstraintViolationException ex) {
    List<FieldErrorResource> errors =
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
            .collect(Collectors.toList());

    return new ErrorResource(errors);
  }

  private String getParam(String s) {
    String[] splits = s.split("\\.");
    if (splits.length == 1) {
      return s;
    } else {
      return String.join(".", Arrays.copyOfRange(splits, 2, splits.length));
    }
  }
}
