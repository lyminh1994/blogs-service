package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.ErrorConstants;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.enumdef.TokenType;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.payload.request.LoginRequest;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.payload.response.AuthenticationResponse;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.util.SecurityUtils;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is implement for the authentication service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userDetailsService;

  private final CookieService cookieService;

  private final EncryptionService encryptionService;

  private final JwtService jwtService;

  @Override
  @Transactional
  public AuthenticationResponse createUser(
      RegisterRequest registerRequest, HttpHeaders responseHeaders) {
    UserEntity user =
        userRepository.save(
            UserEntity.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build());

    UserPrincipal userDetails = UserPrincipal.buildUserDetails(user);
    SecurityUtils.authenticateUser(userDetails);

    String accessToken = updateCookies(user.getUsername(), false, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.buildJwtResponse(encryptedAccessToken, userDetails);
  }

  @Override
  public AuthenticationResponse login(
      String refreshToken, LoginRequest loginRequest, HttpHeaders responseHeaders) {

    String username = loginRequest.getUsername();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, username, loginRequest.getPassword());

    String decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    boolean isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    // If the refresh token is valid, then we will not generate a new refresh token.
    String accessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.buildJwtResponse(encryptedAccessToken);
  }

  @Override
  public AuthenticationResponse refreshToken(String refreshToken, HttpServletRequest request) {
    String decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    boolean refreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    if (!refreshTokenValid) {
      throw new IllegalArgumentException(ErrorConstants.INVALID_TOKEN);
    }
    String username = jwtService.getUsernameFromJwt(decryptedRefreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    SecurityUtils.clearAuthentication();
    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    String accessToken = jwtService.createJwt(username);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.buildJwtResponse(encryptedAccessToken);
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.logout(request, response);
    SecurityUtils.clearAuthentication();
  }

  /**
   * Creates a refresh token if expired and adds it to the cookies.
   *
   * @param username the username
   * @param isRefreshValid if the refresh token is valid
   * @param headers the http headers
   */
  private String updateCookies(String username, boolean isRefreshValid, HttpHeaders headers) {
    if (!isRefreshValid) {
      String refreshToken = jwtService.createJwt(username);
      Duration refreshTokenDuration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

      String encryptedRefreshToken = encryptionService.encrypt(refreshToken);
      cookieService.addCookieToHeaders(
          headers, TokenType.REFRESH, encryptedRefreshToken, refreshTokenDuration);
    }

    return jwtService.createJwt(username);
  }
}
