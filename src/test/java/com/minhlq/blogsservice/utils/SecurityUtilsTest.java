package com.minhlq.blogsservice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
  @WithMockUser
  @DisplayName("Should get Authentication success")
  void shouldGetAuthenticationSuccess() {
    assertNotNull(SecurityUtils.getAuthentication());
  }

  @Test
  @Sql(scripts = "classpath:db/users.sql")
  @WithUserDetails(value = "username1")
  @DisplayName("Should get current user success")
  void shouldGetCurrentUserSuccess() {
    assertNotNull(SecurityUtils.getCurrentUser());
    assertEquals("username1", SecurityUtils.getCurrentUser().getUsername());
  }

  @Test
  @DisplayName("Should get current user return null")
  void shouldGetCurrentUserReturnNull() {
    assertNull(SecurityUtils.getCurrentUser());
  }
}
