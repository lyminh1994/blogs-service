package com.minhlq.blogsservice.payload;

import com.minhlq.blogsservice.entity.UserEntity;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
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

  private static final long serialVersionUID = -6788482996276890023L;

  private Long id;

  @EqualsAndHashCode.Include private String username;

  @EqualsAndHashCode.Include private String email;

  private String password;

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
  public static UserPrincipal buildUserDetails(UserEntity user) {
    Validate.notNull(user, "User must not be null");

    return UserPrincipal.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .bio(user.getBio())
        .image(user.getImage())
        .enabled(true)
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .authorities(Collections.emptyList())
        .build();
  }
}
