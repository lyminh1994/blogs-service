package com.minhlq.blogsservice.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;


class SecurityUtilsTest {

  @BeforeEach
  void setUp() {
    SecurityUtils.clearAuthentication();
  }

  @Test
  void shouldThrowException_whenCallingConstructor() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  void givenNullAuthentication_whenCheckAuthenticated_thenReturnFalse() {
    Assertions.assertFalse(SecurityUtils.isAuthenticated(null));
  }
}
