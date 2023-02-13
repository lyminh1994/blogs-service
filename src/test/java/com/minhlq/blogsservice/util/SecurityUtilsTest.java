package com.minhlq.blogsservice.util;

import com.minhlq.blogsservice.helper.TestHelper;
import com.minhlq.blogsservice.helper.UserHelper;
import com.minhlq.blogsservice.payload.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

class SecurityUtilsTest {

  @BeforeEach
  void setUp() {
    SecurityUtils.clearAuthentication();
  }

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  void testingIsUserAuthenticatedNull() {
    Assertions.assertFalse(SecurityUtils.isAuthenticated(null));
  }

  @Test
  void testingIsUserAuthenticatedNotAuthenticated() {
    Authentication authentication = SecurityUtils.getAuthentication();
    Assertions.assertAll(
        () -> {
          Assertions.assertFalse(SecurityUtils.isAuthenticated());
          Assertions.assertFalse(SecurityUtils.isAuthenticated(authentication));
        });
  }

  @Test
  void testingIsUserAuthenticatedAsAnonymous() {
    TestHelper.setAuthentication(TestHelper.ANONYMOUS_USER, TestHelper.ANONYMOUS_ROLE);
    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void testingIsUserAuthenticatedAuthenticated(TestInfo testInfo) {
    TestHelper.setAuthentication(testInfo.getDisplayName(), TestHelper.ROLE_USER);
    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void testUserDisabledThrowsException() {
    UserPrincipal userPrincipal = UserPrincipal.buildUserDetails(UserHelper.createUser());
    Assertions.assertThrows(
        DisabledException.class, () -> SecurityUtils.validateUserDetailsStatus(userPrincipal));
  }

  @Test
  void testUserEnabledDoesNotTrowsException() {
    Assertions.assertDoesNotThrow(
        () ->
            SecurityUtils.validateUserDetailsStatus(
                UserPrincipal.buildUserDetails(UserHelper.createUser(true))));
  }

  @Test
  void testUserAccountLockedTrowsException() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    userDetails.setAccountNonLocked(false);

    Assertions.assertThrows(
        LockedException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void testUserAccountExpiredTrowsException() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    userDetails.setAccountNonExpired(false);

    Assertions.assertThrows(
        AccountExpiredException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void testUserCredentialsExpiredTrowsException() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    userDetails.setCredentialsNonExpired(false);

    Assertions.assertThrows(
        CredentialsExpiredException.class,
        () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void getAuthorizedUserDetailsWithoutAuthenticationReturnsNull() {
    SecurityUtils.clearAuthentication();
    Assertions.assertNull(SecurityUtils.getAuthenticatedUserDetails());
  }

  @Test
  void authenticateUserWithNullUserDetails() {
    AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(authManager);
          Assertions.assertDoesNotThrow(() -> SecurityUtils.authenticateUser(null));
        });
  }

  @Test
  void authenticateUserWithUserDetails() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));

    SecurityUtils.authenticateUser(userDetails);

    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithNullHttpServletRequestAndUserDetails() {
    SecurityUtils.authenticateUser(null, null);

    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithNullHttpServletRequest() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));

    SecurityUtils.authenticateUser(null, userDetails);

    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithHttpServletRequestAndUserDetails() {
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

    SecurityUtils.authenticateUser(httpServletRequest, userDetails);

    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }
}
