package com.minhlq.blogsservice.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.annotation.impl.validator.DuplicatedUsernameValidator;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import java.util.Optional;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Import(DuplicatedUsernameValidator.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @MockBean private UserRepository userRepository;

  @Mock private AuthService authService;

  @Mock private CookieService cookieService;

  @InjectMocks private AuthController authController;

  private static final Faker FAKER = Faker.instance();
  private static DuplicatedUsernameValidator validator;

  private MockMvc mockMvc;
  private ObjectMapper mapper;
  private String registerUrl;
  private String loginUrl;
  private String refreshTokenUrl;
  private String logoutUrl;
  private String verifyUrl;

  @BeforeEach
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .alwaysDo(MockMvcResultHandlers.print())
            .build();

    validator = new DuplicatedUsernameValidator(userRepository);

    mapper = new ObjectMapper();
    registerUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.REGISTER;
    loginUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGIN;
    refreshTokenUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.REFRESH_TOKEN;
    logoutUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGOUT;
    verifyUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.VERIFY_ACCOUNT;
  }

  @Test
  void givenEmptyRegisterBody_whenCallRegister_thenReturnBadRequest() throws Exception {
    mockMvc.perform(post(registerUrl)).andExpect(status().isBadRequest());
  }

  @Test
  void givenExistedUsername_whenCallRegister_thenReturnBadRequest() throws Exception {
    given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(Optional.empty());
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername(FAKER.name().username());
    registerRequest.setPassword(FAKER.internet().password());

    mockMvc
        .perform(
            post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest());
  }
}
