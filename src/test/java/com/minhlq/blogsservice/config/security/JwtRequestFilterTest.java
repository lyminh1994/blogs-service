package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.entity.UserEntity;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {
  private static final String encryptedToken = "encryptedToken";
  private static final String validBearerToken = SecurityConstants.BEARER_PREFIX + "token";
  private static final String invalidBearerToken = SecurityConstants.BEARER_PREFIX + "invalidToken";
  private static final String API_AUTH_LOGIN =
      SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGIN;

  @Mock private MockFilterChain filterChain;

  @Mock private JwtService jwtService;

  @Mock private UserDetailsService userDetailsService;

  @Mock private EncryptionService encryptionService;

  @InjectMocks private JwtRequestFilter jwtAuthTokenFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeAll
  void beforeAll() throws Exception {
    try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
      Assertions.assertNotNull(mocks);

      request = new MockHttpServletRequest();
      request.setRequestURI(API_AUTH_LOGIN);

      response = new MockHttpServletResponse();
    }
  }

  @Test
  void testDoFilterInternalWhenTokenIsInHeader() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    Mockito.when(jwtService.getJwtToken(request, false)).thenReturn(encryptedToken);
    Mockito.when(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .thenReturn(validBearerToken);
    Mockito.when(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).thenReturn(true);
    Mockito.when(jwtService.getUsernameFromJwt(ArgumentMatchers.anyString()))
        .thenReturn("username");
    UserEntity user =
        UserEntity.builder()
            .id(1L)
            .username("username")
            .password("password")
            .email("email@gmail.com")
            .bio("bio")
            .image("image")
            .build();

    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);
    Mockito.when(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
        .thenReturn(userDetails);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenBearerTokenIsNotValid() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    Mockito.when(jwtService.getJwtToken(request, false)).thenReturn(encryptedToken);
    Mockito.when(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .thenReturn(invalidBearerToken);
    Mockito.when(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).thenReturn(false);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenInvalidEncryptedToken() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, validBearerToken);

    Mockito.when(jwtService.getJwtToken(request, false)).thenReturn(validBearerToken);
    Mockito.when(encryptionService.decrypt(ArgumentMatchers.anyString())).thenReturn(null);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenTokenInCookie() throws ServletException, IOException {
    Mockito.when(jwtService.getJwtToken(request, true)).thenReturn(encryptedToken);
    Mockito.when(encryptionService.decrypt(ArgumentMatchers.anyString()))
        .thenReturn(validBearerToken);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
