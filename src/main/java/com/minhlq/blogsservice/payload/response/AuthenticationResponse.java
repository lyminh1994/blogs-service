package com.minhlq.blogsservice.payload.response;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.util.SecurityUtils;
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
public final class AuthenticationResponse {

  private final UserResponse user;

  private final String type;

  private final String accessToken;

  /**
   * Builds authenticationResponse object from the specified userDetails.
   *
   * @param jwtToken the jwtToken
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse buildJwtResponse(final String jwtToken) {
    return buildJwtResponse(jwtToken, null);
  }

  /**
   * Build authenticationResponse object from the specified userDetails.
   *
   * @param jwToken the jwToken.
   * @param userDetails the userDetails.
   * @return the authenticationResponse.
   */
  public static AuthenticationResponse buildJwtResponse(String jwToken, UserPrincipal userDetails) {
    UserPrincipal localUserDetails = userDetails;
    if (Objects.isNull(localUserDetails)) {
      localUserDetails = SecurityUtils.getAuthenticatedUserDetails();
    }

    if (Objects.nonNull(localUserDetails)) {
      UserResponse user =
          UserResponse.builder()
              .publicId(localUserDetails.getPublicId())
              .email(localUserDetails.getEmail())
              .firstName(localUserDetails.getFirstName())
              .lastName(localUserDetails.getLastName())
              .phone(localUserDetails.getPhone())
              .birthday(localUserDetails.getBirthday())
              .gender(localUserDetails.getGender())
              .profileImage(localUserDetails.getProfileImage())
              .authorities(localUserDetails.getAuthorities())
              .build();

      return AuthenticationResponse.builder()
          .user(user)
          .accessToken(jwToken)
          .type(SecurityConstants.BEARER)
          .build();
    }

    return AuthenticationResponse.builder().build();
  }
}
