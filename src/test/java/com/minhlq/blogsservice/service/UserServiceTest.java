package com.minhlq.blogsservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.response.AuthenticationResponse;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock UserRepository userRepository;

  @Mock PasswordEncoder passwordEncoder;

  @Mock JwtService jwtService;

  @Mock AuthenticationManager authenticationManager;

  @Mock FollowRepository followRepository;

  @InjectMocks UserServiceImpl userService;

  UserEntity user;

  @BeforeEach
  void setUp() {
    user =
        UserEntity.builder()
            .id(1L)
            .username("user01")
            .password("encode-pass")
            .email("user01@example.com")
            .bio("target compelling deliverables")
            .image("sample-image.png")
            .build();
  }

  @Test
  @DisplayName("Should create user success")
  void shouldCreateUserSuccess() {
    // given - precondition or setup
    RegisterRequest registerRequest = new RegisterRequest("user01@example.com", "user01", "pass");
    given(userRepository.save(user)).willReturn(user);
    given(passwordEncoder.encode(registerRequest.getPassword())).willReturn("encode-pass");

    System.out.println(userRepository);
    System.out.println(userService);

    // when -  action or the behaviour that we are going test
    AuthenticationResponse savedUser = userService.createUser(registerRequest);

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
