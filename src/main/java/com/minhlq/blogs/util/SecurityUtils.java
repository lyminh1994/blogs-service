package com.minhlq.blogs.util;

import com.minhlq.blogs.constant.UserConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.model.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

/**
 * This utility class holds custom operations on security used in the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@UtilityClass
public class SecurityUtils {

  public UserDetails buildUserDetails(@NotNull UserEntity user) {
    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities =
        user.getUserRoles().stream()
            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
            .collect(Collectors.toSet());

    return new User(
        user.getUsername(),
        user.getPassword(),
        user.isEnabled(),
        user.getLastSuccessfulLogin().isBefore(LocalDateTime.now().plusDays(30)),
        true,
        user.getFailedLoginAttempts() < 5,
        authorities);
  }

  /**
   * Returns true if the user is authenticated.
   *
   * @param authentication the authentication object
   * @return if user is authenticated
   */
  public boolean isAuthenticated(Authentication authentication) {
    return Objects.nonNull(authentication)
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  /**
   * Returns true if the user is authenticated.
   *
   * @return if user is authenticated
   */
  public boolean isAuthenticated() {
    return isAuthenticated(getAuthentication());
  }

  /**
   * Retrieve the authentication object from the current session.
   *
   * @return authentication
   */
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Sets the provided authentication object to the SecurityContextHolder.
   *
   * @param authentication the authentication
   */
  public void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /** Clears the securityContextHolder. */
  public void clearAuthentication() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  /**
   * Creates an authentication object with the userDetails then set authentication to
   * SecurityContextHolder.
   *
   * @param request the httpServletRequest
   * @param userDetails the userDetails
   */
  public void authenticateUser(HttpServletRequest request, UserDetails userDetails) {
    if (Objects.nonNull(request) && Objects.nonNull(userDetails)) {
      var authorities = userDetails.getAuthorities();
      var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      setAuthentication(authentication);
    }
  }

  /**
   * Creates an authentication object with the credentials then set authentication to
   * SecurityContextHolder.
   *
   * @param authenticationManager the authentication manager
   * @param username the username
   * @param password the password
   */
  public void authenticateUser(
      AuthenticationManager authenticationManager, String username, String password) {
    var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
    var authentication = authenticationManager.authenticate(authenticationToken);

    setAuthentication(authentication);
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    var logoutHandler =
        new CookieClearingLogoutHandler(
            AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);

    var securityContextLogoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, response, null);
    securityContextLogoutHandler.logout(request, response, null);
  }

  /**
   * Validates that the user is neither disabled, locked nor expired.
   *
   * @param userDetails the user details
   */
  public void validateUserDetailsStatus(UserDetails userDetails) {
    log.debug("User details {}", userDetails);

    if (!userDetails.isEnabled()) {
      throw new DisabledException(UserConstants.USER_DISABLED_MESSAGE);
    }
    if (!userDetails.isAccountNonLocked()) {
      throw new LockedException(UserConstants.USER_LOCKED_MESSAGE);
    }
    if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException(UserConstants.USER_EXPIRED_MESSAGE);
    }
  }

  public String getRefreshTokenFromCookies(HttpServletRequest request) {
    var cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }

    return Arrays.stream(cookies)
        .filter(cookie -> TokenType.REFRESH.getName().equals(cookie.getName()))
        .findFirst()
        .map(Cookie::getValue)
        .orElse(null);
  }
}
