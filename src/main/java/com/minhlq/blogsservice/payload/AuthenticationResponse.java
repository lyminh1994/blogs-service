package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.constant.SecurityConstants;

/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record AuthenticationResponse(String type, String accessToken) {

  /**
   * Build authentication response object from the specified userDetails.
   *
   * @param jwToken the jwToken.
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse build(String jwToken) {
    return new AuthenticationResponse(SecurityConstants.BEARER, jwToken);
  }
}
