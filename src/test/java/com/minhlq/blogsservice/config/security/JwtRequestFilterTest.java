package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.constant.TestConstants;
import com.minhlq.blogsservice.helper.UserHelper;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {
    static final String encryptedToken = TestConstants.ENCRYPTED_TOKEN;
    static final String validBearerToken = TestConstants.BEARER_AUTHENTICATION_TOKEN;
    static final String invalidBearerToken = SecurityConstants.BEARER;
    static final String API_AUTH_LOGIN = "/auth/login";

    @Mock
    JwtService jwtService;

    @Mock
    EncryptionService encryptionService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    MockFilterChain filterChain;

    @InjectMocks
    JwtRequestFilter jwtAuthTokenFilter;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeAll
    void beforeAll() throws Exception {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            Assertions.assertNotNull(mocks);

            UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
            given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

            request = new MockHttpServletRequest();
            request.setRequestURI(API_AUTH_LOGIN);

            response = new MockHttpServletResponse();
        }
    }

    @Test
    void givenTokenInHeader_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
        // given - precondition or setup
        request.addHeader(AUTHORIZATION, encryptedToken);

        given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
        given(encryptionService.decrypt(anyString())).willReturn(validBearerToken);
        given(jwtService.isValidJwtToken(anyString())).willReturn(true);
        given(jwtService.getUsernameFromJwt(anyString())).willReturn("user");

        // when - action or the behaviour that we are going test
        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        // then - verify the output
        Assertions.assertEquals(OK.value(), response.getStatus());
    }

    @Test
    void givenTokenInCookie_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
        // given - precondition or setup
        given(jwtService.getJwtToken(request, true)).willReturn(encryptedToken);
        given(encryptionService.decrypt(anyString())).willReturn(validBearerToken);

        // when - action or the behaviour that we are going test
        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        // then - verify the output
        Assertions.assertEquals(OK.value(), response.getStatus());
    }

    @Test
    void givenInvalidToken_whenDoFilterInternal_thenValidJwtTokenReturnFalse()
            throws ServletException, IOException {
        // given - precondition or setup
        request.addHeader(AUTHORIZATION, encryptedToken);

        given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
        given(encryptionService.decrypt(anyString())).willReturn(invalidBearerToken);
        given(jwtService.isValidJwtToken(anyString())).willReturn(false);

        // when - action or the behaviour that we are going test
        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        // then - verify the output
        Assertions.assertEquals(OK.value(), response.getStatus());
    }

    @Test
    void givenRawToken_whenDoFilterInternal_thenDecryptReturnNull()
            throws ServletException, IOException {
        // given - precondition or setup
        request.addHeader(AUTHORIZATION, validBearerToken);

        given(jwtService.getJwtToken(request, false)).willReturn(validBearerToken);
        given(encryptionService.decrypt(anyString())).willReturn(null);

        // when - action or the behaviour that we are going test
        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        // then - verify the output
        Assertions.assertEquals(OK.value(), response.getStatus());
    }
}
