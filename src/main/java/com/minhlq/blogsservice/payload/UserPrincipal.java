package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.entity.User;
import com.minhlq.blogsservice.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserPrincipal builds the userDetails to be used by the application security context.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserPrincipal implements UserDetails {

  private Long id;

  @EqualsAndHashCode.Include private String publicId;

  @EqualsAndHashCode.Include private String username;

  @EqualsAndHashCode.Include private String email;

  private String password;

  private String firstName;

  private String lastName;

  @EqualsAndHashCode.Include private String phone;

  private LocalDate birthday;

  private Gender gender;

  private String profileImage;

  private int failedLoginAttempts;

  private LocalDateTime lastSuccessfulLogin;

  private boolean enabled;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Builds userDetails object from the specified user.
   *
   * @param user the user
   * @return the userDetails
   * @throws NullPointerException if the user is null
   */
  public static UserPrincipal buildUserDetails(final User user) {
    Validate.notNull(user, "User must not be null");

    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities =
        user.getUserRoles().stream()
            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
            .collect(Collectors.toSet());

    return UserPrincipal.builder()
        .id(user.getId())
        .publicId(user.getPublicId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .phone(user.getPhone())
        .birthday(user.getBirthday())
        .gender(user.getGender())
        .profileImage(user.getProfileImage())
        .failedLoginAttempts(user.getFailedLoginAttempts())
        .lastSuccessfulLogin(user.getLastSuccessfulLogin())
        .enabled(user.isEnabled())
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .authorities(authorities)
        .build();
  }
}