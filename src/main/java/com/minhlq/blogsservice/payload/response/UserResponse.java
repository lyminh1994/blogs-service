package com.minhlq.blogsservice.payload.response;

import com.minhlq.blogsservice.enums.Gender;
import com.minhlq.blogsservice.payload.UserPrincipal;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class models the format of the user response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public final class UserResponse {

  private String publicId;

  private String email;

  private String firstName;

  private String lastName;

  private String phone;

  private LocalDate birthday;

  private Gender gender;

  private String profileImage;

  private Collection<? extends GrantedAuthority> authorities;

  public static UserResponse getUserResponse(final UserPrincipal userDetails) {
    Validate.notNull(userDetails, "User details must not be null");

    return UserResponse.builder()
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
  }
}
