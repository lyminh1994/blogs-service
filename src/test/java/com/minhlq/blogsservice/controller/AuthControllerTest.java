package com.minhlq.blogsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private AuthService authService;

  @Mock private CookieService cookieService;

  @InjectMocks private AuthController authController;

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

    mapper = new ObjectMapper();
    registerUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.REGISTER;
    loginUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGIN;
    refreshTokenUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.REFRESH_TOKEN;
    logoutUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGOUT;
    verifyUrl = SecurityConstants.AUTH_ROOT_URL + SecurityConstants.VERIFY_ACCOUNT;
  }

  @Test
  void givenEmptyRegisterBody_whenCallRegister_thenReturnBadRequest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(registerUrl))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void givenExistedUsername_whenCallRegister_thenReturnBadRequest() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(null)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
