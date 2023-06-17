package com.minhlq.blogs.payload;


/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record AuthenticationResponse(String accessToken) {

  /**
   * Build authentication response object from the specified userDetails.
   *
   * @param jwToken the jwToken.
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse build(String jwToken) {
    return new AuthenticationResponse(jwToken);
  }
}
