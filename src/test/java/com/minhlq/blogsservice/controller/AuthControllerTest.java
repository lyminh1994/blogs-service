package com.minhlq.blogsservice.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.response.AuthenticationResponse;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
  @Autowired MockMvc mockMvc;
  @MockBean UserService userService;
  @MockBean JwtService jwtService;
  @MockBean UserDetailsService userDetailsService;
  @MockBean UserRepository userRepository;
  @MockBean AuthenticationManager authenticationManager;

  ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should Create User Success POST request to endpoint - /auth/register")
  void shouldCreateUserSuccess() throws Exception {
    RegisterRequest registerRequest =
        new RegisterRequest("jonathan99@gmail.com", "jonathan99", "123");
    String accessToken = "accessToken";
    String refreshToken = "refreshToken";

    when(userService.createUser(any(RegisterRequest.class)))
        .thenReturn(new AuthenticationResponse(null, accessToken, refreshToken));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.user", nullValue()))
        .andExpect(jsonPath("$.accessToken", is(accessToken)))
        .andExpect(jsonPath("$.refreshToken", is(refreshToken)));
  }

  @Test
  @DisplayName(
      "Should Show Error Message For Blank Username POST request to endpoint - /auth/register")
  void shouldShowErrorMessageForBlankUsername() throws Exception {
    RegisterRequest registerRequest = new RegisterRequest("jonathan99@gmail.com", null, "123");

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors.username[0]", is("can't be empty")));
  }

  @Test
  @DisplayName(
      "Should Show Error Message For Invalid Email POST request to endpoint - /auth/register")
  void shouldShowErrorMessageForInvalidEmail() throws Exception {
    RegisterRequest registerRequest = new RegisterRequest("jonathan99.com", "jonathan99", "123");

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors.email[0]", is("should be an email")));
  }

  @Test
  @DisplayName(
      "Should Show Error For Duplicated Username POST request to endpoint - /auth/register")
  void shouldShowErrorForDuplicatedUsername() throws Exception {
    RegisterRequest registerRequest =
        new RegisterRequest("jonathan99@gmail.com", "jonathan99", "123");
    UserEntity user =
        new UserEntity(1L, "jonathan99", "123", "jonathan99@gmail.com", "bio", "image");
    when(userRepository.findByUsername("jonathan99")).thenReturn(Optional.of(user));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors.username[0]", is("duplicated username")));
  }

  @Test
  @DisplayName("Should Show Error For Duplicated Email POST request to endpoint - /auth/register")
  void shouldShowErrorForDuplicatedEmail() throws Exception {
    RegisterRequest registerRequest =
        new RegisterRequest("jonathan99@gmail.com", "jonathan99", "123");
    UserEntity user =
        new UserEntity(1L, "jonathan99", "123", "jonathan99@gmail.com", "bio", "image");
    when(userRepository.findByEmail("jonathan99@gmail.com")).thenReturn(Optional.of(user));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors.email[0]", is("duplicated email")));
  }

  @Test
  @DisplayName("Should Login Success POST request to endpoint - /auth/login")
  void shouldLoginSuccess() throws Exception {
    LoginRequest loginRequest = new LoginRequest("jonathan99", "123");
    String accessToken = "accessToken";
    String refreshToken = "refreshToken";

    when(userService.login(any(LoginRequest.class)))
        .thenReturn(new AuthenticationResponse(null, accessToken, refreshToken));

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.user", nullValue()))
        .andExpect(jsonPath("$.accessToken", is(accessToken)))
        .andExpect(jsonPath("$.refreshToken", is(refreshToken)));
  }

  @Test
  @DisplayName("Should Fail Login With Wrong Password POST request to endpoint - /auth/login")
  void shouldFailLoginWithWrongPassword() throws Exception {
    LoginRequest loginRequest = new LoginRequest("jonathan99", "456");

    when(userService.login(any(LoginRequest.class))).thenThrow(BadCredentialsException.class);

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.message", is("invalid username or password!")));
  }
}
