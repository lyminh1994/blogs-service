package com.minhlq.blogsservice.payload;

import static com.minhlq.blogsservice.constant.UserConstants.USER_MUST_NOT_BE_NULL;

import com.minhlq.blogsservice.enums.Gender;
import com.minhlq.blogsservice.model.UserEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
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
public record UserPrincipal(
    Long id,
    String publicId,
    String username,
    String email,
    String password,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthday,
    Gender gender,
    String profileImage,
    int failedLoginAttempts,
    LocalDateTime lastSuccessfulLogin,
    boolean enabled,
    boolean accountNonExpired,
    boolean accountNonLocked,
    boolean credentialsNonExpired,
    Collection<? extends GrantedAuthority> authorities)
    implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Builds userDetails object from the specified user.
   *
   * @param user the user
   * @return the userDetails
   * @throws NullPointerException if the user is null
   */
  public static UserPrincipal buildUserDetails(final UserEntity user) {
    Validate.notNull(user, USER_MUST_NOT_BE_NULL);

    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities =
        user.getUserRoles().stream()
            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
            .collect(Collectors.toSet());

    boolean isAccountExpired =
        user.getLastSuccessfulLogin().isAfter(LocalDateTime.now().plusDays(30));

    boolean isAccountLocked = user.getFailedLoginAttempts() > 5;

    return new UserPrincipal(
        user.getId(),
        user.getPublicId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        user.getFirstName(),
        user.getLastName(),
        user.getPhone(),
        user.getBirthday(),
        user.getGender(),
        user.getProfileImage(),
        user.getFailedLoginAttempts(),
        user.getLastSuccessfulLogin(),
        user.isEnabled(),
        !isAccountExpired,
        !isAccountLocked,
        true,
        authorities);
  }
}
