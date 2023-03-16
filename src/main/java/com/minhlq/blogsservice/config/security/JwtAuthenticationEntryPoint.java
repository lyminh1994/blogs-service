package com.minhlq.blogsservice.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.exception.handler.ErrorResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This class implements AuthenticationEntryPoint interface. Then we override the commence method.
 * This method will be triggered anytime unauthenticated User requests a secured endpoint and an
 * AuthenticationException is thrown.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    log.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response
        .getWriter()
        .write(
            objectMapper.writeValueAsString(
                new ErrorResource(
                    null, null, HttpStatus.UNAUTHORIZED, authException.getMessage())));
  }
}
