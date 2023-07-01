package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.ProfileTypeConstants;
import com.minhlq.blogs.constant.SecurityConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the cookie service.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {

  private final Environment environment;

  private final Duration duration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

  /**
   * Creates a servlet cookie from spring httpCookie.
   *
   * @param httpCookie the httpCookie
   * @return the cookie
   */
  @Override
  public Cookie createCookie(@NotNull HttpCookie httpCookie) {
    var cookie = new Cookie(httpCookie.getName(), httpCookie.getValue());
    cookie.setSecure(
        Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.PROD));
    cookie.setHttpOnly(true);

    return cookie;
  }

  /**
   * Creates an httpOnly httpCookie.
   *
   * @param name the name of the cookie
   * @param value the value of the cookie
   * @param maxAge the duration till expiration
   * @return the cookie
   */
  @Override
  public HttpCookie createCookie(@NotNull String name, String value, Duration maxAge) {
    return ResponseCookie.from(name, value)
        .secure(Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.PROD))
        .sameSite("strict")
        .path("/")
        .maxAge(Objects.isNull(maxAge) ? duration : maxAge)
        .httpOnly(true)
        .build();
  }

  /**
   * Creates a cookie with the specified token and token type.
   *
   * @param token the token
   * @param tokenType the type of token
   * @return the cookie
   */
  @Override
  public HttpCookie createTokenCookie(@NotEmpty String token, TokenType tokenType) {
    return createTokenCookie(token, tokenType, duration);
  }

  /**
   * Creates a cookie with the specified token and token type.
   *
   * @param token the token
   * @param tokenType the type of token
   * @param maxAge the duration till expiration
   * @return the cookie
   */
  @Override
  public HttpCookie createTokenCookie(
      @NotEmpty String token, @NotNull TokenType tokenType, Duration maxAge) {
    return createCookie(tokenType.getName(), token, maxAge);
  }

  /**
   * Creates a cookie with the specified token.
   *
   * @param tokenType the token type
   * @return the cookie
   */
  @Override
  public HttpCookie deleteTokenCookie(@NotNull TokenType tokenType) {
    return createCookie(tokenType.getName(), StringUtils.EMPTY, Duration.ZERO);
  }

  /**
   * Creates a cookie with the specified token.
   *
   * @param tokenType the token type
   * @return the httpHeaders
   */
  @Override
  public HttpHeaders addDeletedCookieToHeaders(@NotNull TokenType tokenType) {
    var httpCookie = deleteTokenCookie(tokenType);
    var httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());
    return httpHeaders;
  }

  /**
   * creates a cookie with the specified token and tokenType then adds it to headers.
   *
   * @param tokenType the tokenType
   * @param token the token
   * @return the httpHeaders
   */
  @Override
  public HttpHeaders addCookieToHeaders(@NotNull TokenType tokenType, @NotEmpty String token) {
    return addCookieToHeaders(tokenType, token, duration);
  }

  /**
   * creates a cookie with the specified token and tokenType then adds it to headers.
   *
   * @param tokenType the tokenType
   * @param token the token
   * @param maxAge the duration till expiration
   * @return the httpHeaders
   */
  @Override
  public HttpHeaders addCookieToHeaders(
      @NotNull TokenType tokenType, @NotEmpty String token, Duration maxAge) {
    var httpHeaders = new HttpHeaders();
    addCookieToHeaders(
        httpHeaders, tokenType, token, Objects.isNull(maxAge) ? duration : maxAge);

    return httpHeaders;
  }

  /**
   * creates a cookie with the specified token and type with duration then adds it to the headers.
   *
   * @param httpHeaders the httpHeaders
   * @param tokenType the tokenType
   * @param token the token
   * @param maxAge the duration till expiration
   */
  @Override
  public void addCookieToHeaders(
      HttpHeaders httpHeaders, @NotNull TokenType tokenType, String token, Duration maxAge) {
    httpHeaders.add(HttpHeaders.SET_COOKIE, createTokenCookie(token, tokenType, maxAge).toString());
  }
}
