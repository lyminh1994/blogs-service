package com.minhlq.blogs.service;

import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@Disabled("not ready yet")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock UserRepository userRepository;

  @Mock PasswordEncoder passwordEncoder;

  @InjectMocks UserServiceImpl userService;

  UserEntity user;

  @BeforeEach
  void setUp() {
    user = new UserEntity();
  }

  @Test
  @DisplayName("Should create user success")
  void shouldCreateUserSuccess() {}

  @Test
  @DisplayName("Should login success")
  void shouldLoginSuccess() {}

  @Test
  @DisplayName("Should update profile success")
  void shouldUpdateProfileSuccess() {}

  @Test
  @DisplayName("Should update profile throw resource not found exception")
  void shouldUpdateProfileThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should find by username success")
  void shouldFindByUsernameSuccess() {}

  @Test
  @DisplayName("Should find by username throw resource not found exception")
  void shouldFindByUsernameThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should follow by username success")
  void shouldFollowByUsernameSuccess() {}

  @Test
  @DisplayName("Should follow by username throw resource not found exception")
  void shouldFollowByUsernameThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should un follow by username success")
  void shouldUnFollowByUsernameSuccess() {}

  @Test
  @DisplayName("Should un follow by username throw resource not found exception")
  void shouldUnFollowByUsernameThrowResourceNotFoundException() {}
}
