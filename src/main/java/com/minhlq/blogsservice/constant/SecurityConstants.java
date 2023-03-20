package com.minhlq.blogsservice.constant;

import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * This class holds all security-related URL mappings constants.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

  public static final String BEARER = "Bearer";

  public static final int DEFAULT_TOKEN_DURATION = 7;
  public static final int SECURITY_STRENGTH = 12;

  public static final List<String> HTTP_METHODS_ALLOWED = List.of("GET", "POST", "PUT", "DELETE");
  public static final List<String> HTTP_HEADERS_ALLOWED =
      List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CACHE_CONTROL, HttpHeaders.CONTENT_TYPE);
  private static final String[] PUBLIC_MATCHERS = {
    "/resources/**",
    "/static/**",
    "/actuator/**",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    AppConstants.SIGN_UP,
    AppConstants.SIGN_IN,
    AppConstants.REFRESH_TOKEN,
    AppConstants.SIGN_OUT,
    AppConstants.VERIFY
  };

  /**
   * Public matchers to allow access to the application.
   *
   * @return public matchers.
   */
  public static Collection<String> getPublicMatchers() {
    return List.of(PUBLIC_MATCHERS);
  }
}
