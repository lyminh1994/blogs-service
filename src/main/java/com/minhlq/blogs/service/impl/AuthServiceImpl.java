package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.SecurityConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.payload.AuthenticationResponse;
import com.minhlq.blogs.payload.SignInRequest;
import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.AuthService;
import com.minhlq.blogs.service.CookieService;
import com.minhlq.blogs.service.JwtService;
import com.minhlq.blogs.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
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
    userRepository.saveAndFlush(user);

    var isRefreshTokenValid = false;
    if (StringUtils.isNotEmpty(refreshToken)) {
      isRefreshTokenValid = jwtService.isValidJwtToken(refreshToken);
    }

    // If the refresh token is valid, then we will not generate a new refresh token.
    var accessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);

    return AuthenticationResponse.build(accessToken);
  }

  @Override
  public AuthenticationResponse refreshAccessToken(
      String refreshToken, HttpServletRequest request) {
    var refreshTokenValid = jwtService.isValidJwtToken(refreshToken);

    if (!refreshTokenValid) {
      throw new IllegalArgumentException("Invalid token");
    }

    var username = jwtService.getUsernameFromJwt(refreshToken);
    var userDetails = userDetailsService.loadUserByUsername(username);

    SecurityUtils.clearAuthentication();
    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    return AuthenticationResponse.build(jwtService.createJwt(username));
  }

  @Override
  public void signOut(HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.logout(request, response);
    SecurityUtils.clearAuthentication();
  }

  @Override
  @Transactional
  public void activeAccount(String verificationToken) {
    if (StringUtils.isBlank(verificationToken) || !jwtService.isValidJwtToken(verificationToken)) {
      throw new SecurityException("Verification token was expire");
    }

    var user =
        userRepository
            .findByVerificationTokenAndEnabled(verificationToken, false)
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
          jwtService.createJwt(username, Instant.now().plusSeconds(refreshTokenMaxAge.toSeconds()));

      cookieService.addCookieToHeaders(
          responseHeaders, TokenType.REFRESH, refreshToken, refreshTokenMaxAge);
    }

    return jwtService.createJwt(username);
  }
}
