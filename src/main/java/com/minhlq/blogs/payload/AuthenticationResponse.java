package com.minhlq.blogs.payload;

/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record AuthenticationResponse(String accessToken, String tokenType, Long expiresIn) {

  /**
   * Build authentication response object from the specified userDetails.
   *
   * @param jwToken access token
   * @param tokenType kind of token.
   * @param expiresIn token expired in millisecond
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse build(String jwToken, Long expiresIn) {
    return new AuthenticationResponse(jwToken, "Bearer", expiresIn);
  }
}
