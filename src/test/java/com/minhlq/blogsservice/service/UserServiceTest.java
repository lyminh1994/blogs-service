package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.AuthenticationResponse;
import com.minhlq.blogsservice.payload.SignUpRequest;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock UserRepository userRepository;

  @Mock PasswordEncoder passwordEncoder;

  @Mock AuthService authService;

  @InjectMocks UserServiceImpl userService;

  UserEntity user;

  @BeforeEach
  void setUp() {
    user = new UserEntity();
  }

  @Test
  @DisplayName("Should create user success")
  void shouldCreateUserSuccess() {
    // given - precondition or setup
    SignUpRequest signUpRequest = new SignUpRequest("user01", "pass", "email@gmail.com");
    given(userRepository.save(user)).willReturn(user);
    given(passwordEncoder.encode(signUpRequest.password())).willReturn("encode-pass");

    // when -  action or the behaviour that we are going test
    AuthenticationResponse savedUser = authService.createUser(signUpRequest, new HttpHeaders());

    System.out.println(savedUser);
    // then - verify the output
    assertThat(savedUser).isNotNull();
  }

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
