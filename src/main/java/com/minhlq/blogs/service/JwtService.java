package com.minhlq.blogs.service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;

/**
 * This is the contract for the jwt service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface JwtService {

  /**
   * Generate a JwtToken for the specified user.
   *
   * @param username the username
   * @return the token
   */
  String createJwt(String username);

  /**
   * Generate a JwtToken for the specified user.
   *
   * @param username the user details
   * @param expiration the expiration date
   * @return the token
   */
  String createJwt(String username, Instant expiration);

  /**
   * Retrieve username from the token.
   *
   * @param token the token
   * @return the username
   */
  String getUsernameFromJwt(String token);

  /**
   * Retrieves the token from the request cookie or request header if present and valid.
   *
   * @param request the httpRequest
   * @param fromCookie if token should be retrieved from the cookies.
   * @return the token token
   */
  String getJwtToken(HttpServletRequest request, boolean fromCookie);

  /**
   * Validates the Jwt token passed to it.
   *
   * @param token the token
   * @return if valid or not
   */
  boolean isValidJwtToken(String token);
}
