package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.TestConstants;
import com.minhlq.blogsservice.helper.UserHelper;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {

  static final String encryptedToken = TestConstants.ENCRYPTED_TOKEN;
  static final String validBearerToken = TestConstants.BEARER_AUTHENTICATION_TOKEN;
  static final String invalidBearerToken = TestConstants.INVALID_TOKEN;

  @Mock JwtService jwtService;
  @Mock EncryptionService encryptionService;
  @Mock UserDetailsService userDetailsService;
  @Mock MockFilterChain filterChain;

  @InjectMocks JwtRequestFilter jwtAuthTokenFilter;

  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();

  @BeforeAll
  void beforeAll() {
    AutoCloseable mocks = openMocks(this);
    assertNotNull(mocks);

    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);
  }

  @Test
  void givenTokenInHeader_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    given(encryptionService.decrypt(anyString())).willReturn(validBearerToken);
    given(jwtService.isValidJwtToken(anyString())).willReturn(true);
    given(jwtService.getUsernameFromJwt(anyString())).willReturn("user");

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(200, response.getStatus());
  }

  @Test
  void givenTokenInCookie_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, true)).willReturn(encryptedToken);
    given(encryptionService.decrypt(anyString())).willReturn(validBearerToken);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(200, response.getStatus());
  }

  @Test
  void givenInvalidToken_whenDoFilterInternal_thenValidJwtTokenReturnFalse()
      throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    given(encryptionService.decrypt(anyString())).willReturn(invalidBearerToken);
    given(jwtService.isValidJwtToken(anyString())).willReturn(false);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(200, response.getStatus());
  }

  @Test
  void givenRawToken_whenDoFilterInternal_thenDecryptReturnNull()
      throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, validBearerToken);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false)).willReturn(validBearerToken);
    given(encryptionService.decrypt(anyString())).willReturn(null);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(200, response.getStatus());
  }
}
