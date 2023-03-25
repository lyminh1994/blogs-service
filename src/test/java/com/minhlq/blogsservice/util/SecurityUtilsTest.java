package com.minhlq.blogsservice.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.minhlq.blogsservice.enums.UserRole;
import com.minhlq.blogsservice.helper.TestHelper;
import com.minhlq.blogsservice.helper.UserHelper;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

class SecurityUtilsTest {

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
  void givenAuthenticatedIsNull_whenIsAuthenticated_thenReturnFalse() {
    assertFalse(SecurityUtils.isAuthenticated(null));
  }

  @Test
  void givenNotAuthenticated_whenIsAuthenticated_thenReturnFalse() {
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
  void givenValidUser_whenIsAuthenticated_thenReturnTrue(TestInfo testInfo) {
    TestHelper.setAuthentication(testInfo.getDisplayName(), UserRole.ROLE_USER.name());
    assertTrue(SecurityUtils.isAuthenticated());
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
  void givenWithoutAuthentication_whenGetAuthorizedUserDetails_thenReturnNull() {
    SecurityUtils.clearAuthentication();
    assertNull(SecurityUtils.getAuthenticatedUserDetails());
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
}
