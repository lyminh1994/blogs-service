package com.minhlq.blogsservice.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
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

/**
 * A global exception handler for REST API.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles IllegalArgumentException and IllegalStateException thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String message = ex.getMessage();
    return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  /**
   * Handles InvalidRequestException thrown by the REST API.
   *
   * @param ex The exception thrown by the REST API.
   * @param request The request object.
   * @return A ResponseEntity object.
   */
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

  /**
   * Handles BadCredentialsException thrown by the AuthenticationManager.
   *
   * @param ex The exception thrown by the REST API.
   * @return A ErrorMessage.
   */
  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String handleInvalidAuthentication(BadCredentialsException ex) {
    log.error("Invalid authentication:", ex);
    return "Invalid username or password";
  }

  /**
   * Override the response for MethodArgumentNotValidException.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param ex the exception
   * @param headers the headers to be written to the response
   * @param status the selected response status
   * @param request the current request
   * @return a {@code ResponseEntity} instance
   */
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

  /**
   * Handles ConstraintViolationException thrown by the ConstraintValidator.
   *
   * @param ex The exception thrown by the REST API.
   * @return A ErrorMessage.
   */
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
    }

    return String.join(".", Arrays.copyOfRange(splits, 2, splits.length));
  }
}
