package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.annotation.Loggable;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.payload.request.LoginRequest;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.payload.response.AuthenticationResponse;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class attempt to authenticate with AuthenticationManager bean, add authentication object to
 * SecurityContextHolder then Generate JWT token, then return JWT to client.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@SecurityRequirements
@RequiredArgsConstructor
@RequestMapping(SecurityConstants.AUTH_ROOT_URL)
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

  private final AuthService authService;

  private final CookieService cookieService;

  /**
   * Creates a new user and return JWT token.
   *
   * @param registerRequest the register
   * @return the jwt token details
   */
  @Loggable
  @PostMapping(SecurityConstants.REGISTER)
  @Operation(summary = "Register", description = "Register new account")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody @Valid RegisterRequest registerRequest) {
    HttpHeaders responseHeaders = new HttpHeaders();
    AuthenticationResponse authenticationResponse =
        authService.createUser(registerRequest, responseHeaders);
    return ResponseEntity.status(HttpStatus.CREATED)
        .headers(responseHeaders)
        .body(authenticationResponse);
  }

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * <p>A refresh token is generated and returned as a cookie.
   *
   * @param refreshToken the refresh token
   * @param loginRequest the login request
   * @return the jwt token details
   */
  @Loggable
  @PostMapping(SecurityConstants.LOGIN)
  @Operation(summary = "Login", description = "Authentication user and return access information")
  public ResponseEntity<AuthenticationResponse> login(
      @CookieValue(required = false) String refreshToken,
      @RequestBody @Valid LoginRequest loginRequest) {

    HttpHeaders responseHeaders = new HttpHeaders();
    AuthenticationResponse authenticationResponse =
        authService.login(refreshToken, loginRequest, responseHeaders);
    return ResponseEntity.ok().headers(responseHeaders).body(authenticationResponse);
  }

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param refreshToken The refresh token
   * @param request The request
   * @return the jwt token details
   */
  @Loggable
  @GetMapping(SecurityConstants.REFRESH_TOKEN)
  @Operation(summary = "Refresh token", description = "Create and return new access token")
  public ResponseEntity<AuthenticationResponse> refreshToken(
      @CookieValue String refreshToken, HttpServletRequest request) {
    AuthenticationResponse authenticationResponse = authService.refreshToken(refreshToken, request);
    return ResponseEntity.ok().body(authenticationResponse);
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  @Loggable
  @DeleteMapping(SecurityConstants.LOGOUT)
  @Operation(summary = "Logout", description = "Logout and clear cookie to headers")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    HttpHeaders responseHeaders = cookieService.addDeletedCookieToHeaders(TokenType.REFRESH);
    return ResponseEntity.noContent().headers(responseHeaders).build();
  }

  /**
   * Verify account by verification token.
   *
   * @param verificationToken the token
   */
  @Loggable
  @GetMapping(SecurityConstants.VERIFY_ACCOUNT)
  @Operation(summary = "Verify", description = "Verification account by provided token")
  public ResponseEntity<Void> verify(@PathVariable String verificationToken) {
    authService.verificationAccount(verificationToken);
    return ResponseEntity.ok().build();
  }
}
