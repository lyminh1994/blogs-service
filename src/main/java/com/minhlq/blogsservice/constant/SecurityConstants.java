package com.minhlq.blogsservice.constant;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

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
  public static final String BEARER_PREFIX = "Bearer ";

  public static final String API_ROOT_URL_MAPPING = "/**";

  public static final String AUTH_ROOT_URL = "/auth";
  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
  public static final String REFRESH_TOKEN = "/refresh-token";
  public static final String LOGOUT = "/logout";
  public static final String ROOT_PATH = "/";
  public static final String SAME_SITE = "strict";

  public static final int DEFAULT_TOKEN_DURATION = 7;
  public static final int SECURITY_STRENGTH = 12;

  /** The token type cannot be null. */
  public static final String THE_TOKEN_TYPE_CANNOT_BE_NULL = "The tokenType cannot be null";

  /** The token cannot be null or empty. */
  public static final String THE_TOKEN_CANNOT_BE_NULL_OR_EMPTY =
      "The token cannot be null or empty";

  private static final String[] PUBLIC_MATCHERS = {
    "/resources/**",
    "/static/**",
    "/actuator/**",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    String.join("/", AUTH_ROOT_URL, "**"),
  };

  public static final List<String> ALLOWED_HTTP_METHODS =
      List.of(
          HttpMethod.GET.name(),
          HttpMethod.POST.name(),
          HttpMethod.PUT.name(),
          HttpMethod.DELETE.name(),
          HttpMethod.PATCH.name(),
          HttpMethod.OPTIONS.name());

  public static final List<String> ALLOWED_HTTP_HEADERS =
      List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CACHE_CONTROL, HttpHeaders.CONTENT_TYPE);

  /**
   * Public matchers to allow access to the application.
   *
   * @return public matchers.
   */
  public static Collection<String> getPublicMatchers() {
    return Collections.unmodifiableCollection(Arrays.asList(PUBLIC_MATCHERS));
  }
}
