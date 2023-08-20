package com.minhlq.blogs.util;

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
import com.minhlq.blogs.model.RoleEntity;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.model.UserRoleEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
  void givenUser_whenCallBuildUserDetails_thenReturnUserDetails() {
    // given UserEntity
    UserEntity mockUser = mock(UserEntity.class);
    when(mockUser.getUsername()).thenReturn("testuser");
    when(mockUser.getPassword()).thenReturn("testpassword");
    when(mockUser.isEnabled()).thenReturn(false);
    when(mockUser.getLastSuccessfulLogin()).thenReturn(LocalDateTime.now());
    when(mockUser.getFailedLoginAttempts()).thenReturn(6);

    RoleEntity mockRole = mock(RoleEntity.class);
    when(mockRole.getName()).thenReturn("ROLE_USER");

    UserRoleEntity mockUserRole = mock(UserRoleEntity.class);
    when(mockUserRole.getRole()).thenReturn(mockRole);

    Set<UserRoleEntity> userRoles = new HashSet<>();
    userRoles.add(mockUserRole);
    when(mockUser.getUserRoles()).thenReturn(userRoles);

    // when call buildUserDetails
    UserDetails actual = SecurityUtils.buildUserDetails(mockUser);

    // then
    assertNotNull(actual);
    assertEquals("testuser", actual.getUsername());
    assertEquals("testpassword", actual.getPassword());
    assertFalse(actual.isEnabled());
    assertTrue(actual.isAccountNonExpired());
    assertFalse(actual.isAccountNonLocked());
    assertTrue(actual.isCredentialsNonExpired());
    assertEquals(1, actual.getAuthorities().size());
    assertTrue(actual.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
  }

  @Test
  void whenWithNullAuthentication_thenIsAuthenticatedFalse() {
    boolean actual = SecurityUtils.isAuthenticated(null);

    assertFalse(actual);
  }

  @Test
  void givenAuthenticatedAuthentication_thenIsAuthenticatedTrue() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    boolean actual = SecurityUtils.isAuthenticated(authentication);

    assertTrue(actual);
  }

  @Test
  void givenAnonymousAuthenticationToken_thenIsAuthenticatedFalse() {
    AnonymousAuthenticationToken anonymousToken = mock(AnonymousAuthenticationToken.class);
    when(anonymousToken.isAuthenticated()).thenReturn(true);
    boolean actual = SecurityUtils.isAuthenticated(anonymousToken);

    assertFalse(actual);
  }

  @Test
  void givenUnauthenticatedAuthentication_thenIsAuthenticatedFalse() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);
    boolean actual = SecurityUtils.isAuthenticated(authentication);

    assertFalse(actual);
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
    assertFalse(SecurityUtils.isAuthenticated());
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
