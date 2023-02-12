package com.minhlq.blogsservice.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

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
  String createJwt(String username, Date expiration);

  /**
   * Retrieve username from the token.
   *
   * @param jwt the token
   * @return the username
   */
  String getUsernameFromJwt(String jwt);

  /**
   * Retrieves the jwt token from the request cookie or request header if present and valid.
   *
   * @param request the httpRequest
   * @param fromCookie if jwt should be retrieved from the cookies.
   * @return the jwt token
   */
  String getJwtToken(HttpServletRequest request, boolean fromCookie);

  /**
   * Validates the Jwt token passed to it.
   *
   * @param jwt the token
   * @return if valid or not
   */
  boolean isValidJwtToken(String jwt);
}
