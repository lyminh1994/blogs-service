package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.payload.AuthenticationResponse;
import com.minhlq.blogsservice.payload.SignInRequest;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is implement for the authentication service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userDetailsService;

  private final CookieService cookieService;

  private final EncryptionService encryptionService;

  private final JwtService jwtService;

  @Override
  @Transactional
  public AuthenticationResponse signIn(
      String refreshToken, SignInRequest requestBody, HttpHeaders responseHeaders) {

    var username = requestBody.username();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, username, requestBody.password());

    // Update user last successful login and reset failed login attempts
    var user = userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    user.setLastSuccessfulLogin(LocalDateTime.now());
    user.setFailedLoginAttempts(0);
    userRepository.save(user);

    var isRefreshTokenValid = false;
    if (StringUtils.isNotEmpty(refreshToken)) {
      var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
      isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);
    }

    // If the refresh token is valid, then we will not generate a new refresh token.
    var accessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);
    var encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.build(encryptedAccessToken);
  }

  @Override
  public AuthenticationResponse refreshAccessToken(
      String refreshToken, HttpServletRequest request) {
    var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    var refreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    if (!refreshTokenValid) {
      throw new IllegalArgumentException("Invalid token");
    }

    var username = jwtService.getUsernameFromJwt(decryptedRefreshToken);
    var userDetails = userDetailsService.loadUserByUsername(username);

    SecurityUtils.clearAuthentication();
    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    var accessToken = jwtService.createJwt(username);
    var encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.build(encryptedAccessToken);
  }

  @Override
  public void signOut(HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.logout(request, response);
    SecurityUtils.clearAuthentication();
  }

  @Override
  @Transactional
  public void activeAccount(String verificationToken) {
    var decodedToken = encryptionService.decode(verificationToken);
    if (StringUtils.isBlank(decodedToken) || !jwtService.isValidJwtToken(decodedToken)) {
      throw new SecurityException("Verification token was expire");
    }

    var user =
        userRepository
            .findByVerificationTokenAndEnabled(decodedToken, false)
            .orElseThrow(ResourceNotFoundException::new);

    user.setVerificationToken(null);
    user.setEnabled(true);
    userRepository.save(user);
  }

  /**
   * Creates a refresh token if expired and adds it to the cookies.
   *
   * @param username the username
   * @param isRefreshTokenValid if the refresh token is valid
   * @param responseHeaders the http response headers
   */
  private String updateCookies(
      String username, boolean isRefreshTokenValid, HttpHeaders responseHeaders) {
    if (!isRefreshTokenValid) {
      var refreshTokenMaxAge = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);
      var refreshToken =
          jwtService.createJwt(
              username, Date.from(Instant.now().plusSeconds(refreshTokenMaxAge.toSeconds())));

      var encryptedRefreshToken = encryptionService.encrypt(refreshToken);
      cookieService.addCookieToHeaders(
          responseHeaders, TokenType.REFRESH, encryptedRefreshToken, refreshTokenMaxAge);
    }

    return jwtService.createJwt(username);
  }
}
