package com.minhlq.blogsservice.utils;

import com.minhlq.blogsservice.dto.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class SecurityUtils {

  /**
   * Get current authentication information
   *
   * @return Current authentication
   */
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Get current login user information
   *
   * @return The information of the current login user, null when doesn't login
   */
  public UserPrincipal getCurrentUser() {
    if (getAuthentication() != null) {
      Object userInfo = getAuthentication().getPrincipal();
      if (userInfo instanceof UserDetails) {
        return (UserPrincipal) userInfo;
      }
    }

    return null;
  }
}
