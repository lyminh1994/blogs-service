package com.minhlq.blogs.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import com.minhlq.blogs.constant.TestConstants;
import com.minhlq.blogs.helper.UserHelper;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.service.JwtService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {

  @Mock JwtService jwtService;
  @Mock UserDetailsService userDetailsService;
  @Mock MockFilterChain filterChain;

  @InjectMocks JwtRequestFilter jwtAuthTokenFilter;

  MockHttpServletRequest request;
  MockHttpServletResponse response;

  @BeforeAll
  void beforeAll() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    AutoCloseable mocks = openMocks(this);
    assertNotNull(mocks);

    UserPrincipal userDetails = UserPrincipal.buildUserDetails(UserHelper.createUser(true));
    given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);
  }

  @Test
  void givenTokenInHeader_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, TestConstants.BEARER_AUTHENTICATION_TOKEN);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false))
        .willReturn(TestConstants.BEARER_AUTHENTICATION_TOKEN);
    given(jwtService.isValidJwtToken(anyString())).willReturn(true);
    given(jwtService.getUsernameFromJwt(anyString())).willReturn("user");

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenTokenInCookie_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, TestConstants.BEARER_AUTHENTICATION_TOKEN);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, true))
        .willReturn(TestConstants.BEARER_AUTHENTICATION_TOKEN);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenInvalidToken_whenDoFilterInternal_thenValidJwtTokenReturnFalse()
      throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, TestConstants.BEARER_AUTHENTICATION_TOKEN);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false))
        .willReturn(TestConstants.BEARER_AUTHENTICATION_TOKEN);
    given(jwtService.isValidJwtToken(anyString())).willReturn(false);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenRawToken_whenDoFilterInternal_thenDecryptReturnNull()
      throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, TestConstants.BEARER_AUTHENTICATION_TOKEN);

    // given - precondition or setup
    given(jwtService.getJwtToken(request, false)).willReturn(null);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
