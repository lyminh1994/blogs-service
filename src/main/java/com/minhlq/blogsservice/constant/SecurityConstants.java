package com.minhlq.blogsservice.constant;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

  public static final String TOKEN_TYPE_CANNOT_BE_NULL = "The token type cannot be null";
  public static final String TOKEN_CANNOT_BE_NULL_OR_EMPTY = "The token cannot be null or empty";
  public static final String HTTP_COOKIE_CANNOT_BE_NULL = "The http Cookie cannot be null";
  public static final String NAME_CANNOT_BE_NULL_OR_EMPTY = "The name cannot be null or empty";
  public static final List<String> HTTP_METHODS_ALLOWED =
      List.of(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name());
  public static final List<String> HTTP_HEADERS_ALLOWED =
      List.of(AUTHORIZATION, CACHE_CONTROL, CONTENT_TYPE);
  private static final String[] PUBLIC_MATCHERS = {
    "/resources/**", "/static/**", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/auth/**",
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
