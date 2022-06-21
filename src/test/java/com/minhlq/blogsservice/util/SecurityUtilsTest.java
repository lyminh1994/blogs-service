package com.minhlq.blogsservice.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
class SecurityUtilsTest {

  @Test
  void shouldThrowException_whenCallingConstructor() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  @WithMockUser
  void shouldGetAuthenticationSuccess() {
    Assertions.assertNotNull(SecurityUtils.getAuthentication());
  }

  @Test
  @Sql(scripts = "classpath:db/users.sql")
  @WithUserDetails(value = "username1")
  void shouldGetCurrentUserSuccess() {
    Assertions.assertNotNull(SecurityUtils.getAuthenticatedUserDetails());
    Assertions.assertEquals("username1", SecurityUtils.getAuthenticatedUserDetails().getUsername());
  }

  @Test
  void shouldGetCurrentUserReturnNull() {
    Assertions.assertNull(SecurityUtils.getAuthenticatedUserDetails());
  }
}
