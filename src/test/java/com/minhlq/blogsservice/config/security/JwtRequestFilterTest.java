package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.constant.TestConstants;
import com.minhlq.blogsservice.helper.UserHelper;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {
  private static final String encryptedToken = TestConstants.ENCRYPTED_TOKEN;
  private static final String validBearerToken = TestConstants.BEARER_AUTHENTICATION_TOKEN;
  private static final String invalidBearerToken = SecurityConstants.BEARER_PREFIX;
  private static final String API_AUTH_LOGIN =
      SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGIN;

  @Mock private JwtService jwtService;

  @Mock private EncryptionService encryptionService;

  @Mock private UserDetailsService userDetailsService;

  @Mock private MockFilterChain filterChain;

  @InjectMocks private JwtRequestFilter jwtAuthTokenFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeAll
  void beforeAll() throws Exception {
    try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
      Assertions.assertNotNull(mocks);

      UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
      BDDMockito.given(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
          .willReturn(userDetails);

      request = new MockHttpServletRequest();
      request.setRequestURI(API_AUTH_LOGIN);

      response = new MockHttpServletResponse();
    }
  }

  @Test
  void givenTokenInHeader_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    // given - precondition or setup
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    BDDMockito.given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    BDDMockito.given(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .willReturn(validBearerToken);
    BDDMockito.given(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).willReturn(true);
    BDDMockito.given(jwtService.getUsernameFromJwt(ArgumentMatchers.anyString()))
        .willReturn("user");

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenTokenInCookie_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    // given - precondition or setup
    BDDMockito.given(jwtService.getJwtToken(request, true)).willReturn(encryptedToken);
    BDDMockito.given(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .willReturn(validBearerToken);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenInvalidToken_whenDoFilterInternal_thenValidJwtTokenReturnFalse()
      throws ServletException, IOException {
    // given - precondition or setup
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    BDDMockito.given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    BDDMockito.given(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .willReturn(invalidBearerToken);
    BDDMockito.given(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).willReturn(false);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenRawToken_whenDoFilterInternal_thenDecryptReturnNull()
      throws ServletException, IOException {
    // given - precondition or setup
    request.addHeader(HttpHeaders.AUTHORIZATION, validBearerToken);

    BDDMockito.given(jwtService.getJwtToken(request, false)).willReturn(validBearerToken);
    BDDMockito.given(encryptionService.decrypt(ArgumentMatchers.anyString())).willReturn(null);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
