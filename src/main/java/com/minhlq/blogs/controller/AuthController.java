package com.minhlq.blogs.controller;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.payload.AuthenticationResponse;
import com.minhlq.blogs.payload.LoginRequest;
import com.minhlq.blogs.payload.RegisterRequest;
import com.minhlq.blogs.service.AuthService;
import com.minhlq.blogs.service.CookieService;
import com.minhlq.blogs.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@RequestMapping(AppConstants.AUTHENTICATION_ENDPOINT)
@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

  private final AuthService authService;

  private final CookieService cookieService;

  /**
   * Creates a new user and return JWT token.
   *
   * @param registerRequest the register
   */
  @PostMapping(AppConstants.REGISTER_ENDPOINT)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Register", description = "Create new account")
  public void register(@RequestBody @Valid RegisterRequest registerRequest) {
    authService.register(registerRequest);
  }

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * <p>A refresh token is generated and returned as a cookie.
   *
   * @param loginRequest the login request
   * @param httpRequest The request
   * @return the jwt token details
   */
  @PostMapping(AppConstants.LOGIN_ENDPOINT)
  @Operation(summary = "Login", description = "Authentication account and return access token")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpRequest) {
    var responseHeaders = new HttpHeaders();
    var body =
        authService.login(
            SecurityUtils.getRefreshTokenFromCookies(httpRequest), loginRequest, responseHeaders);
    return ResponseEntity.ok().headers(responseHeaders).body(body);
  }

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param httpRequest The request
   * @return the jwt token details
   */
  @GetMapping(AppConstants.REFRESH_TOKEN_ENDPOINT)
  @Operation(summary = "Refresh access token", description = "Create and return new access token")
  public AuthenticationResponse refreshAccessToken(HttpServletRequest httpRequest) {
    return authService.getAccessToken(
        SecurityUtils.getRefreshTokenFromCookies(httpRequest), httpRequest);
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param httpRequest the http request
   * @param httpResponse the http response
   */
  @DeleteMapping(AppConstants.LOGOUT_ENDPOINT)
  @Operation(summary = "Logout", description = "Clear cookie of user browser")
  public ResponseEntity<Void> logout(
      HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    authService.logout(httpRequest, httpResponse);
    return ResponseEntity.noContent()
        .headers(cookieService.addDeletedCookieToHeaders(TokenType.REFRESH))
        .build();
  }

  /**
   * Verify account by verification token.
   *
   * @param verifyToken the token
   */
  @GetMapping(AppConstants.VERIFY_ENDPOINT)
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Operation(summary = "Verify account", description = "Active account by provided token in email")
  public void verifyAccount(@PathVariable String verifyToken) {
    authService.activeAccount(verifyToken);
  }
}
