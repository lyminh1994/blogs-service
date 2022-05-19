package com.minhlq.blogsservice.payload.response;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.util.SecurityUtils;
import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

/**
 * This class models the format of the login response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class AuthenticationResponse implements Serializable {

  private static final long serialVersionUID = -818779525319891506L;

  private final UserPrincipal user;

  private final String type;

  private final String accessToken;

  /**
   * Builds jwtResponseBuilder object from the specified userDetails.
   *
   * @param jwtToken the jwtToken
   * @return the jwtResponse
   */
  public static AuthenticationResponse buildJwtResponse(final String jwtToken) {
    return buildJwtResponse(jwtToken, null);
  }

  /**
   * Build jwtResponse object from the specified userDetails.
   *
   * @param jwToken the jwToken.
   * @param userDetails the userDetails.
   * @return the jwtResponse object.
   */
  public static AuthenticationResponse buildJwtResponse(String jwToken, UserPrincipal userDetails) {
    UserPrincipal localUserDetails = userDetails;
    if (Objects.isNull(localUserDetails)) {
      localUserDetails = SecurityUtils.getAuthenticatedUserDetails();
    }

    if (Objects.nonNull(localUserDetails)) {
      return AuthenticationResponse.builder()
          .user(localUserDetails)
          .accessToken(jwToken)
          .type(SecurityConstants.BEARER)
          .build();
    }

    return AuthenticationResponse.builder().build();
  }
}
