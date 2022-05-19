package com.minhlq.blogsservice.config.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    log.error("Unauthorized error: {}", authException.getMessage());

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
  }
}
