package com.minhlq.blogsservice.config.security;

import static org.mockito.BDDMockito.given;

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
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestInstance(Lifecycle.PER_CLASS)
class JwtRequestFilterTest {
  private static final String encryptedToken =
      "rsZ/WlOikdMCoomZbQdyJFlraMy3Dk8Pyw5vbgUPJLXA6r4fwA29isOtQvzjq1W1vVscvchCS3ci3qCQ/pQTHvx8AiCvUrxqpIr3KBwSgTc4jw+92eTgRUG9zAYVDTE4UKSIENQ2jwNVI59Rpg4Iw5NJbhIhWgqU2lFIDFCv9xM4DkYwRt7E3W7K+7Py3MaHHRQ3SYRZGggqnif3aE51mNxTMqrRWmn8qQOKiJ8Y8OI8IzORlDCfeomyOFq2y0C5nt+nqzE2bm7cQdZ9LSDhx37lK5X7ZNE/9DaF8I/nQ/0=";
  private static final String validBearerToken =
      SecurityConstants.BEARER_PREFIX
          + "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMDAxIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.aLAFz9x6OF4BSpUQxIKWn39sDp8Cr9ZEeVqcMTdwtBsEZwdCKyR2lkjo_2BzcOJz96N7Qdx7t7VUXerKShnbxg";
  private static final String invalidBearerToken =
      SecurityConstants.BEARER_PREFIX
          + "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMDAxIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0";
  private static final String API_AUTH_LOGIN =
      SecurityConstants.AUTH_ROOT_URL + SecurityConstants.LOGIN;

  @InjectMocks private JwtRequestFilter jwtAuthTokenFilter;

  @Mock private JwtService jwtService;

  @Mock private EncryptionService encryptionService;

  @Mock private UserDetailsService userDetailsService;

  @Mock private MockFilterChain filterChain;

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
  void givenTokenInHeader_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    // given - precondition or setup
    request.addHeader(HttpHeaders.AUTHORIZATION, encryptedToken);

    given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    given(encryptionService.decrypt(ArgumentMatchers.anyString())).willReturn(validBearerToken);
    given(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).willReturn(true);
    given(jwtService.getUsernameFromJwt(ArgumentMatchers.anyString())).willReturn("user001");
    UserEntity user = new UserEntity();

    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);
    given(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
        .willReturn(userDetails);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void givenTokenInCookie_whenDoFilterInternal_thenReturnOK() throws ServletException, IOException {
    // given - precondition or setup
    given(jwtService.getJwtToken(request, true)).willReturn(encryptedToken);
    given(encryptionService.decrypt(ArgumentMatchers.anyString())).willReturn(validBearerToken);

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

    given(jwtService.getJwtToken(request, false)).willReturn(encryptedToken);
    given(encryptionService.decrypt(ArgumentMatchers.anyString())).willReturn(invalidBearerToken);
    given(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).willReturn(false);

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

    given(jwtService.getJwtToken(request, false)).willReturn(validBearerToken);
    given(encryptionService.decrypt(ArgumentMatchers.anyString())).willReturn(null);

    // when - action or the behaviour that we are going test
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    // then - verify the output
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
