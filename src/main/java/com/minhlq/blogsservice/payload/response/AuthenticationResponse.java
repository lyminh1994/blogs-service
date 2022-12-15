package com.minhlq.blogsservice.payload.response;

import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.util.SecurityUtils;
import lombok.Data;

import java.util.Objects;

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

    private final UserResponse user;

    private final String type;

    private final String accessToken;

    /**
     * Builds authenticationResponse object from the specified userDetails.
     *
     * @param jwtToken the jwtToken
     * @return the authenticationResponse.
     */
    public static AuthenticationResponse build(final String jwtToken) {
        return build(jwtToken, null);
    }

    /**
     * Build authentication response object from the specified userDetails.
     *
     * @param jwToken     the jwToken.
     * @param userDetails the userDetails.
     * @return the authenticationResponse.
     */
    public static AuthenticationResponse build(String jwToken, UserPrincipal userDetails) {
        if (Objects.isNull(userDetails)) {
            userDetails = SecurityUtils.getAuthenticatedUserDetails();
        }

        if (Objects.nonNull(userDetails)) {
            UserResponse user =
                    UserResponse.builder()
                            .publicId(userDetails.getPublicId())
                            .email(userDetails.getEmail())
                            .firstName(userDetails.getFirstName())
                            .lastName(userDetails.getLastName())
                            .phone(userDetails.getPhone())
                            .birthday(userDetails.getBirthday())
                            .gender(userDetails.getGender())
                            .profileImage(userDetails.getProfileImage())
                            .authorities(userDetails.getAuthorities())
                            .build();

            return new AuthenticationResponse(user, BEARER, jwToken);
        }

        return null;
    }
}
