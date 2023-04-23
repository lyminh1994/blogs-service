package com.minhlq.blogs.controller;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.payload.AuthenticationResponse;
import com.minhlq.blogs.payload.SignInRequest;
import com.minhlq.blogs.payload.SignUpRequest;
import com.minhlq.blogs.service.AuthService;
import com.minhlq.blogs.service.CookieService;
import com.minhlq.blogs.service.UserService;
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
  public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpBody) {
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
      @RequestBody @Valid SignInRequest requestBody, HttpServletRequest request) {
    var responseHeaders = new HttpHeaders();
    var body =
        authService.signIn(
            SecurityUtils.getRefreshTokenFromCookies(request), requestBody, responseHeaders);
    return ResponseEntity.ok().headers(responseHeaders).body(body);
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
    return authService.refreshAccessToken(
        SecurityUtils.getRefreshTokenFromCookies(request), request);
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
    authService.signOut(request, response);
    return ResponseEntity.noContent()
        .headers(cookieService.addDeletedCookieToHeaders(TokenType.REFRESH))
        .build();
  }

  /**
   * Verify account by verification token.
   *
   * @param verifyToken the token
   */
  @GetMapping(AppConstants.VERIFY)
  @Operation(summary = "Verify account", description = "Active account by provided token in email")
  public ResponseEntity<Void> verify(@PathVariable String verifyToken) {
    authService.activeAccount(verifyToken);
    return ResponseEntity.accepted().build();
  }
}
