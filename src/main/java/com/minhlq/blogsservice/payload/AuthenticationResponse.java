package com.minhlq.blogsservice.payload;

import lombok.Data;

import static com.minhlq.blogsservice.constant.SecurityConstants.BEARER;

/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public final class AuthenticationResponse {

  private final String type;

  private final String accessToken;

  /**
   * Build authentication response object from the specified userDetails.
   *
   * @param jwToken the jwToken.
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse build(String jwToken) {
    return new AuthenticationResponse(BEARER, jwToken);
  }
}
