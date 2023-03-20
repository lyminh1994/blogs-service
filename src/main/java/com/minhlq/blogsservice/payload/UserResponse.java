package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.enums.Gender;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

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
  public static UserResponse getUserResponse(@NotNull UserPrincipal userDetails) {

    return new UserResponse(
        userDetails.publicId(),
        userDetails.email(),
        userDetails.firstName(),
        userDetails.lastName(),
        userDetails.phone(),
        userDetails.birthday(),
        userDetails.gender(),
        userDetails.profileImage(),
        userDetails.authorities());
  }
}
