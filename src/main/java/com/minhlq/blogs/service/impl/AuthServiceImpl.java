package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.constant.SecurityConstants;
import com.minhlq.blogs.constant.UserConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.enums.UserRole;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.payload.AuthenticationResponse;
import com.minhlq.blogs.payload.LoginRequest;
import com.minhlq.blogs.payload.RegisterRequest;
import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.AuthService;
import com.minhlq.blogs.service.CookieService;
import com.minhlq.blogs.service.JwtService;
import com.minhlq.blogs.service.RoleService;
import com.minhlq.blogs.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

  @Value("${jwt.config.ttl}")
  private Long ttl;

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userDetailsService;

  private final CookieService cookieService;

  private final JwtService jwtService;

  private final RoleService roleService;

  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void register(RegisterRequest body) {
    var role = roleService.findByName(UserRole.ROLE_USER);

    var verificationToken = UUID.randomUUID().toString();
    var uri =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(AppConstants.AUTHENTICATION_ENDPOINT + AppConstants.VERIFY_ENDPOINT)
            .buildAndExpand(verificationToken)
            .toUri();
    log.debug("Active account link: {}", uri);

    var user =
        UserEntity.builder()
            .username(body.username())
            .password(passwordEncoder.encode(body.password()))
            .email(body.email())
            .verificationToken(verificationToken)
            .expiredVerificationToken(
                LocalDateTime.now().plusDays(UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION))
            .build();

    user.addRole(role);

    userRepository.save(user);
  }

  @Override
  @Transactional
  public AuthenticationResponse login(String refreshToken, LoginRequest body, HttpHeaders headers) {
    var username = body.username();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, username, body.password());

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
    var accessToken = updateCookies(username, isRefreshTokenValid, headers);

    return AuthenticationResponse.build(accessToken, ttl);
  }

  @Override
  public AuthenticationResponse getAccessToken(String refreshToken, HttpServletRequest request) {
    var refreshTokenValid = jwtService.isValidJwtToken(refreshToken);
    if (!refreshTokenValid) {
      throw new IllegalArgumentException("Invalid token");
    }

    var username = jwtService.getUsernameFromJwt(refreshToken);
    var userDetails = userDetailsService.loadUserByUsername(username);

    SecurityUtils.clearAuthentication();
    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    return AuthenticationResponse.build(jwtService.createJwt(username), ttl);
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.logout(request, response);
    SecurityUtils.clearAuthentication();
  }

  @Override
  @Transactional
  public void activeAccount(String verifyToken) {
    var user = userRepository.findByVerificationTokenAndEnabled(verifyToken, false);
    if (user.isEmpty() || LocalDateTime.now().isAfter(user.get().getExpiredVerificationToken())) {
      throw new SecurityException("Verification token was expire");
    }

    user.get().setVerificationToken(null);
    user.get().setEnabled(true);
    userRepository.saveAndFlush(user.get());
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
