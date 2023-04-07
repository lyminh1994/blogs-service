package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.constant.AppConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.payload.AuthenticationResponse;
import com.minhlq.blogsservice.payload.SignInRequest;
import com.minhlq.blogsservice.payload.SignUpRequest;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.util.SecurityUtils;
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
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

  private final UserService userService;

  private final AuthService authService;

  private final CookieService cookieService;

  /**
   * Creates a new user and return JWT token.
   *
   * @param signUpBody the register
   * @return the jwt token details
   */
  @PostMapping(AppConstants.SIGN_UP)
  @Operation(summary = "Sign up", description = "Create new account")
  public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpBody) {
    userService.createUser(signUpBody);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * <p>A refresh token is generated and returned as a cookie.
   *
   * @param requestBody the login request
   * @param request The request
   * @return the jwt token details
   */
  @PostMapping(AppConstants.SIGN_IN)
  @Operation(
      summary = "Sign in",
      description = "Authentication account and return access information")
  public ResponseEntity<AuthenticationResponse> signIn(
      @Valid @RequestBody SignInRequest requestBody, HttpServletRequest request) {
    var refreshToken = SecurityUtils.getRefreshTokenFromCookies(request);

    var responseHeaders = new HttpHeaders();
    var result = authService.signIn(refreshToken, requestBody, responseHeaders);
    return ResponseEntity.ok().headers(responseHeaders).body(result);
  }

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param request The request
   * @return the jwt token details
   */
  @GetMapping(AppConstants.REFRESH_TOKEN)
  @Operation(
      summary = "Refresh access token",
      description = "Create and return new access information")
  public AuthenticationResponse refreshToken(HttpServletRequest request) {
    var refreshToken = SecurityUtils.getRefreshTokenFromCookies(request);

    return authService.refreshAccessToken(refreshToken, request);
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the http request
   * @param response the http response
   */
  @DeleteMapping(AppConstants.SIGN_OUT)
  @Operation(summary = "Sign out", description = "Sign out and clear cookie of user browser")
  public ResponseEntity<Void> signOut(HttpServletRequest request, HttpServletResponse response) {
    var responseHeaders = cookieService.addDeletedCookieToHeaders(TokenType.REFRESH);
    authService.signOut(request, response);
    return ResponseEntity.noContent().headers(responseHeaders).build();
  }

  /**
   * Verify account by verification token.
   *
   * @param verifyToken the token
   */
  @GetMapping(AppConstants.VERIFY)
  @Operation(summary = "Verify account", description = "Active account by provided token in email")
  public void verify(@PathVariable String verifyToken) {
    authService.activeAccount(verifyToken);
  }
}
