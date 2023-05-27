package com.minhlq.blogs.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.enums.UserRole;
import com.minhlq.blogs.helper.TestHelper;
import com.minhlq.blogs.helper.UserHelper;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.payload.UserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

class SecurityUtilsTest {

  HttpServletRequest request = mock(HttpServletRequest.class);

  @BeforeEach
  void setUp() {
    SecurityUtils.clearAuthentication();
  }

  @Test
  void whenCallingConstructor_thenThrowUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  void givenNullAuthentication_whenIsAuthenticated_thenReturnFalse() {
    assertFalse(SecurityUtils.isAuthenticated(null));
  }

  @Test
  void givenUnauthenticatedUser_whenIsAuthenticated_thenReturnFalse() {
    Authentication authentication = SecurityUtils.getAuthentication();
    assertAll(
        () -> {
          assertFalse(SecurityUtils.isAuthenticated());
          assertFalse(SecurityUtils.isAuthenticated(authentication));
        });
  }

  @Test
  void givenAnonymousUser_whenIsAuthenticated_thenReturnFalse() {
    TestHelper.setAuthentication("anonymous", UserRole.ROLE_ANONYMOUS.name());
    assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void getAuthentication_ReturnsAuthenticationFromSecurityContextHolder() {
    // Create a mock Authentication object
    Authentication expected = mock(Authentication.class);

    // Create a mock SecurityContext
    SecurityContext securityContext = mock(SecurityContext.class);

    // Set the mock SecurityContext in SecurityContextHolder
    SecurityContextHolder.setContext(securityContext);

    // Set the mock Authentication object in the mock SecurityContext
    when(securityContext.getAuthentication()).thenReturn(expected);

    // Call the method to be tested
    Authentication actual = SecurityUtils.getAuthentication();

    // Verify that the method returned the expected Authentication object
    assertEquals(expected, actual);
  }

  @Test
  void setAuthentication_SetsAuthenticationInSecurityContextHolder() {
    // Create a mock Authentication object
    Authentication authentication = mock(Authentication.class);

    // Create a mock SecurityContext
    SecurityContext securityContext = mock(SecurityContext.class);

    // Set the mock SecurityContext in SecurityContextHolder
    SecurityContextHolder.setContext(securityContext);

    // Call the method to be tested
    SecurityUtils.setAuthentication(authentication);

    // Verify that the authentication object was set in the SecurityContext
    verify(securityContext).setAuthentication(authentication);
  }

  @Test
  void givenWithoutAuthentication_whenGetAuthorizedUserDetails_thenReturnNull() {
    SecurityUtils.clearAuthentication();
    assertNull(SecurityUtils.getAuthenticatedUserDetails());
  }

  @Test
  void clearAuthentication_SetsAuthenticationToNull() {
    // Create a mock Authentication object
    Authentication authentication = mock(Authentication.class);

    // Set the mock Authentication object in SecurityContextHolder
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Call the method to be tested
    SecurityUtils.clearAuthentication();

    // Verify that the authentication object in SecurityContextHolder is null
    Authentication actual = SecurityContextHolder.getContext().getAuthentication();
    assertNull(actual);
  }

  @Test
  void givenAuthenticateUserWithNullUserDetails_whenAuthenticateUser_thenDoesNotThrowException() {
    AuthenticationManager authManager = mock(AuthenticationManager.class);

    assertAll(
        () -> {
          assertNotNull(authManager);
          assertDoesNotThrow(() -> SecurityUtils.authenticateUser(null));
        });
  }

  @Test
  void givenAuthenticateUserWithUserDetails_whenIsAuthenticated_thenReturnTrue() {
    UserEntity user = UserHelper.createUser(true);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);
    SecurityUtils.authenticateUser(userDetails);

    assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void
      givenAuthenticateUserWithNullHttpServletRequestAndUserDetails_whenIsAuthenticated_thenReturnFalse() {
    SecurityUtils.authenticateUser(null, null);

    assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void givenAuthenticateUserWithNullHttpServletRequest_whenIsAuthenticated_thenReturnFalse() {
    UserEntity user = UserHelper.createUser(true);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);

    SecurityUtils.authenticateUser(null, userDetails);

    assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void
      givenAuthenticateUserWithHttpServletRequestAndUserDetails_whenIsAuthenticated_thenReturnTrue() {
    UserEntity user = UserHelper.createUser(true);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    SecurityUtils.authenticateUser(httpServletRequest, userDetails);

    assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void getAuthenticatedUserDetails_WhenNotAuthenticated_ReturnsNull() {
    // Call the method to be tested
    UserPrincipal actual = SecurityUtils.getAuthenticatedUserDetails();

    // Verify that the actual is null
    assertNull(actual);
  }

  @Test
  void givenDisabledUser_whenValidateUserDetailsStatus_thenThrowsDisabledException() {
    UserPrincipal userPrincipal = UserPrincipal.buildUserDetails(UserHelper.createUser());
    assertThrows(
        DisabledException.class, () -> SecurityUtils.validateUserDetailsStatus(userPrincipal));
  }

  @Test
  void givenEnabledUser_whenValidateUserDetailsStatus_thenDoesNotThrowsException() {
    assertDoesNotThrow(
        () ->
            SecurityUtils.validateUserDetailsStatus(
                UserPrincipal.buildUserDetails(UserHelper.createUser(true))));
  }

  @Test
  void givenUserFailedLoginAttemptsIs7_whenValidateUserDetailsStatus_thenThrowsLockedException() {
    UserEntity user = UserHelper.createUser(true);
    user.setFailedLoginAttempts(7);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);

    assertThrows(LockedException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void
      givenUserLastSuccessfulLoginPlus31_whenValidateUserDetailsStatus_thenThrowsAccountExpiredException() {
    UserEntity user = UserHelper.createUser(true);
    user.setLastSuccessfulLogin(LocalDateTime.now().plusDays(31));
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);

    assertThrows(
        AccountExpiredException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void validateUserDetailsStatus_WithEnabledUser_DoesNotThrowException() {
    // Create a mock UserDetails object with all flags enabled
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.isEnabled()).thenReturn(true);
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isAccountNonExpired()).thenReturn(true);

    // Call the method to be tested and verify that no exception is thrown
    assertDoesNotThrow(() -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void validateUserDetailsStatus_WithDisabledUser_ThrowsDisabledException() {
    // Create a mock UserDetails object with the isEnabled flag set to false
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.isAccountNonExpired()).thenReturn(true);
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isEnabled()).thenReturn(false);

    // Call the method to be tested and verify that a DisabledException is thrown
    assertThrows(
        DisabledException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void validateUserDetailsStatus_WithLockedUser_ThrowsLockedException() {
    // Create a mock UserDetails object with the isAccountNonLocked flag set to false
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.isAccountNonExpired()).thenReturn(true);
    when(userDetails.isAccountNonLocked()).thenReturn(false);
    when(userDetails.isEnabled()).thenReturn(true);

    // Call the method to be tested and verify that a LockedException is thrown
    assertThrows(LockedException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void validateUserDetailsStatus_WithExpiredUser_ThrowsAccountExpiredException() {
    // Create a mock UserDetails object with the isAccountNonExpired flag set to false
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.isAccountNonExpired()).thenReturn(false);
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isEnabled()).thenReturn(true);

    // Call the method to be tested and verify that an AccountExpiredException is thrown
    assertThrows(
        AccountExpiredException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void getRefreshTokenFromCookies_WithValidCookie_ReturnsTokenValue() {
    // Create a mock cookie
    Cookie refreshCookie = new Cookie(TokenType.REFRESH.getName(), "refresh_token_value");

    // Set the cookies in the mock HttpServletRequest
    when(request.getCookies()).thenReturn(new Cookie[] {refreshCookie});

    // Call the method to be tested
    String actual = SecurityUtils.getRefreshTokenFromCookies(request);

    // Verify the result
    assertNotNull(actual);
    assertEquals("refresh_token_value", actual);
  }

  @Test
  void getRefreshTokenFromCookies_WithNoCookies_ReturnsNull() {
    // Set the cookies in the mock HttpServletRequest to null
    when(request.getCookies()).thenReturn(null);

    // Call the method to be tested
    String actual = SecurityUtils.getRefreshTokenFromCookies(request);

    // Verify the result
    assertNull(actual);
  }

  @Test
  void getRefreshTokenFromCookies_WithNoMatchingCookie_ReturnsNull() {
    // Create a mock cookie with a different name
    Cookie invalidCookie = new Cookie("other_token", "other_token_value");

    // Set the cookies in the mock HttpServletRequest
    when(request.getCookies()).thenReturn(new Cookie[] {invalidCookie});

    // Call the method to be tested
    String actual = SecurityUtils.getRefreshTokenFromCookies(request);

    // Verify the result
    assertNull(actual);
  }
}
