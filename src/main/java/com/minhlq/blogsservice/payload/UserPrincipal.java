package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.entity.RoleEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.enums.Gender;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserPrincipal implements UserDetails {

  private Long id;

  @EqualsAndHashCode.Include private String username;

  @EqualsAndHashCode.Include private String email;

  private String password;

  private String firstName;

  private String lastName;

  private String phone;

  private Instant birthday;

  private Gender gender;

  private String bio;

  private String image;

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
  public static UserPrincipal buildUserDetails(UserEntity user, Set<RoleEntity> roles) {
    Validate.notNull(user, "User must not be null");

    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities =
        roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());

    return UserPrincipal.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .phone(user.getPhone())
        .birthday(user.getBirthday())
        .gender(user.getGender())
        .bio(user.getBio())
        .image(user.getImage())
        .enabled(user.isStatus())
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .authorities(authorities)
        .build();
  }
}
