package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.RoleEntity;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.AuthenticationResponse;
import com.minhlq.blogsservice.payload.SignInRequest;
import com.minhlq.blogsservice.payload.SignUpRequest;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.RoleService;
import com.minhlq.blogsservice.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static com.minhlq.blogsservice.constant.ErrorConstants.INVALID_TOKEN;
import static com.minhlq.blogsservice.constant.ErrorConstants.VERIFY_TOKEN_EXPIRED;
import static com.minhlq.blogsservice.constant.SecurityConstants.DEFAULT_TOKEN_DURATION;
import static com.minhlq.blogsservice.constant.UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION;

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

  private final RoleService roleService;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userDetailsService;

  private final CookieService cookieService;

  private final EncryptionService encryptionService;

  private final JwtService jwtService;

  @Override
  @Transactional
  public AuthenticationResponse createUser(SignUpRequest signUpBody, HttpHeaders responseHeaders) {
    RoleEntity role = roleService.findByName("ROLE_USER");
    Duration ttl = Duration.ofDays(DAYS_TO_ALLOW_ACCOUNT_ACTIVATION);

    String verificationToken =
        jwtService.createJwt(
            signUpBody.username(), Date.from(Instant.now().plusSeconds(ttl.toSeconds())));

    UserEntity user = new UserEntity();
    user.setUsername(signUpBody.username());
    user.setPassword(passwordEncoder.encode(signUpBody.password()));
    user.setEmail(signUpBody.email());
    user.setVerificationToken(encryptionService.encode(verificationToken));
    user.addRole(role);

    UserEntity savedUser = userRepository.saveAndFlush(user);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(savedUser);
    SecurityUtils.authenticateUser(userDetails);

    String accessToken = updateCookies(savedUser.getUsername(), false, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.build(encryptedAccessToken);
  }

  @Override
  @Transactional
  public AuthenticationResponse signIn(
      String refreshToken, SignInRequest requestBody, HttpHeaders responseHeaders) {

    String username = requestBody.username();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, username, requestBody.password());

    // Update user last successful login and reset failed login attempts
    UserEntity user =
        userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    user.setLastSuccessfulLogin(LocalDateTime.now());
    user.setFailedLoginAttempts(0);
    userRepository.save(user);

    boolean isRefreshTokenValid = false;
    if (StringUtils.isNotEmpty(refreshToken)) {
      String decryptedRefreshToken = encryptionService.decrypt(refreshToken);
      isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);
    }

    // If the refresh token is valid, then we will not generate a new refresh token.
    String accessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.build(encryptedAccessToken);
  }

  @Override
  public AuthenticationResponse refreshAccessToken(
      String refreshToken, HttpServletRequest request) {
    String decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    boolean refreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    if (!refreshTokenValid) {
      throw new IllegalArgumentException(INVALID_TOKEN);
    }

    String username = jwtService.getUsernameFromJwt(decryptedRefreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    SecurityUtils.clearAuthentication();
    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    String accessToken = jwtService.createJwt(username);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

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
    String decodedToken = encryptionService.decode(verificationToken);

    if (StringUtils.isBlank(decodedToken) || !jwtService.isValidJwtToken(decodedToken)) {
      throw new SecurityException(VERIFY_TOKEN_EXPIRED);
    }

    UserEntity user =
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
      Duration refreshTokenMaxAge = Duration.ofDays(DEFAULT_TOKEN_DURATION);
      String refreshToken =
          jwtService.createJwt(
              username, Date.from(Instant.now().plusSeconds(refreshTokenMaxAge.toSeconds())));

      String encryptedRefreshToken = encryptionService.encrypt(refreshToken);
      cookieService.addCookieToHeaders(
          responseHeaders, TokenType.REFRESH, encryptedRefreshToken, refreshTokenMaxAge);
    }

    return jwtService.createJwt(username);
  }
}
