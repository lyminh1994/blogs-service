package com.minhlq.blogs.payload;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record AuthenticationResponse(
    UserResponse user, String accessToken, String tokenType, Long expiresIn) {

  /**
   * Build authentication response object from the specified userDetails.
   *
   * @param user response user details
   * @param jwToken access token
   * @param expiresIn token expired in millisecond
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse build(UserResponse user, String jwToken, Long expiresIn) {
    return new AuthenticationResponse(
        user, jwToken, OAuth2AccessToken.TokenType.BEARER.getValue(), expiresIn);
  }
}
