package com.minhlq.blogs.config.jpa;

import com.minhlq.blogs.util.SecurityUtils;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

/**
 * This class gets the application's current auditor which is the username of the authenticated
 * user.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@EqualsAndHashCode
public final class ApplicationAuditorAware implements AuditorAware<String> {

  /**
   * Returns the current auditor of the application.
   *
   * @return the current auditor
   */
  @NonNull
  @Override
  public Optional<String> getCurrentAuditor() {

    // Check if there is a user logged in.
    // If so, use the logged-in user as the current auditor.
    // spring injects an anonymousUser if there is no authentication and authorization
    if (SecurityUtils.isAuthenticated()) {
      return Optional.ofNullable(SecurityUtils.getAuthentication().getName());
    }

    // If there is no authentication, then the system will be used as the current auditor.
    return Optional.of("SYSTEM");
  }
}
