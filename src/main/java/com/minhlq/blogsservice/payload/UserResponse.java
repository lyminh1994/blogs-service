package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.enums.Gender;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

import static com.minhlq.blogsservice.constant.UserConstants.USER_DETAILS_MUST_NOT_BE_NULL;

/**
 * This class models the format of the user response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record UserResponse(
    String publicId,
    String email,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthday,
    Gender gender,
    String profileImage,
    Collection<? extends GrantedAuthority> authorities) {
  public static UserResponse getUserResponse(final UserPrincipal userDetails) {
    Validate.notNull(userDetails, USER_DETAILS_MUST_NOT_BE_NULL);

    return new UserResponse(
        userDetails.getPublicId(),
        userDetails.getEmail(),
        userDetails.getFirstName(),
        userDetails.getLastName(),
        userDetails.getPhone(),
        userDetails.getBirthday(),
        userDetails.getGender(),
        userDetails.getProfileImage(),
        userDetails.getAuthorities());
  }
}
