package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.ErrorConstants;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.constant.UserConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.RoleEntity;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.payload.request.LoginRequest;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.payload.response.AuthenticationResponse;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.RoleService;
import com.minhlq.blogsservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This is implement for the authentication service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
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
  public AuthenticationResponse createUser(
      RegisterRequest registerRequest, HttpHeaders responseHeaders) {
    RoleEntity role = roleService.findByName("ROLE_USER");
    Duration verificationTokenDuration =
        Duration.ofDays(UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION);

    String verificationToken =
        jwtService.createJwt(
            registerRequest.getUsername(),
            Date.from(Instant.now().plusSeconds(verificationTokenDuration.toSeconds())));

    UserEntity user = new UserEntity();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setVerificationToken(encryptionService.encode(verificationToken));
    user.addRole(role);

    UserEntity savedUser = userRepository.saveAndFlush(user);
    UserPrincipal userDetails = UserPrincipal.buildUserDetails(savedUser);
    SecurityUtils.authenticateUser(userDetails);

    String accessToken = updateCookies(savedUser.getUsername(), false, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(accessToken);

    return AuthenticationResponse.buildJwtResponse(encryptedAccessToken, userDetails);
  }

  @Override
  @Transactional
  public AuthenticationResponse login(
      String refreshToken, LoginRequest loginRequest, HttpHeaders responseHeaders) {

    String username = loginRequest.getUsername();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, username, loginRequest.getPassword());

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

    return AuthenticationResponse.buildJwtResponse(encryptedAccessToken);
  }

  @Override
  public AuthenticationResponse refreshAccessToken(
      String refreshToken, HttpServletRequest request) {
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

  @Override
  @Transactional
  public void verificationAccount(String verificationToken) {
    String decodedToken = encryptionService.decode(verificationToken);

    if (StringUtils.isBlank(decodedToken) || !jwtService.isValidJwtToken(decodedToken)) {
      throw new SecurityException(ErrorConstants.VERIFY_TOKEN_EXPIRED);
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
   * @param headers the http headers
   */
  private String updateCookies(String username, boolean isRefreshTokenValid, HttpHeaders headers) {
    if (!isRefreshTokenValid) {
      Duration refreshTokenDuration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);
      String refreshToken =
          jwtService.createJwt(
              username, Date.from(Instant.now().plusSeconds(refreshTokenDuration.toSeconds())));

      String encryptedRefreshToken = encryptionService.encrypt(refreshToken);
      cookieService.addCookieToHeaders(
          headers, TokenType.REFRESH, encryptedRefreshToken, refreshTokenDuration);
    }

    return jwtService.createJwt(username);
  }
}
