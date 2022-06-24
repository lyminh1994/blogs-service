package com.minhlq.blogsservice.util;

import com.minhlq.blogsservice.constant.ErrorConstants;
import com.minhlq.blogsservice.payload.UserPrincipal;
import java.util.Collection;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Log4j2
@UtilityClass
public class SecurityUtils {

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
   * @param userDetails the userDetails
   */
  public void authenticateUser(UserPrincipal userDetails) {
    if (Objects.nonNull(userDetails)) {
      Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

      setAuthentication(authentication);
    }
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
      Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
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
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    setAuthentication(authentication);
  }

  /**
   * Returns the user details from the authenticated object if authenticated.
   *
   * @return the user details
   */
  public UserPrincipal getAuthenticatedUserDetails() {
    if (isAuthenticated()) {
      return (UserPrincipal) getAuthentication().getPrincipal();
    }

    log.warn(ErrorConstants.UNAUTHORIZED_ACCESS);
    return null;
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    String rememberMeCookieKey = AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
    CookieClearingLogoutHandler logoutHandler =
        new CookieClearingLogoutHandler(rememberMeCookieKey);

    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
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
      throw new DisabledException("User is disabled");
    }
    if (!userDetails.isAccountNonLocked()) {
      throw new LockedException("User is locked");
    }
    if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException("User is expired");
    }
    if (!userDetails.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException("User credentials expired");
    }
  }
}
