package com.minhlq.blogsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.annotation.impl.validator.DuplicatedUsernameValidator;
import com.minhlq.blogsservice.payload.request.SignUpRequest;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
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

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(DuplicatedUsernameValidator.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final Faker FAKER = new Faker();
    private static DuplicatedUsernameValidator validator;
    @MockBean
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private CookieService cookieService;
    @InjectMocks
    private AuthController authController;
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
        registerUrl = "";
        loginUrl = "";
        refreshTokenUrl = "";
        logoutUrl = "";
        verifyUrl = "";
    }

    @Test
    void givenEmptyRegisterBody_whenCallRegister_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post(registerUrl)).andExpect(status().isBadRequest());
    }

    @Test
    void givenExistedUsername_whenCallRegister_thenReturnBadRequest() throws Exception {
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(FAKER.name().username());
        signUpRequest.setPassword(FAKER.internet().password());

        mockMvc
                .perform(
                        post(registerUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest());
    }
}
