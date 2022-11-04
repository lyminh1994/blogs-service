package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.payload.request.LoginRequest;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.payload.response.AuthenticationResponse;
import com.minhlq.blogsservice.service.AuthService;
import com.minhlq.blogsservice.service.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

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
@RequestMapping("/auth")
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
  @PostMapping("/register")
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
   * @param loginRequest the login request
   * @return the jwt token details
   */
  @PostMapping("/login")
  @Operation(summary = "Login", description = "Authentication user and return access information")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
    String refreshToken =
        Arrays.stream(request.getCookies())
            .filter(cookie -> TokenType.REFRESH.getName().equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

    HttpHeaders responseHeaders = new HttpHeaders();
    AuthenticationResponse authenticationResponse =
        authService.login(refreshToken, loginRequest, responseHeaders);
    return ResponseEntity.ok().headers(responseHeaders).body(authenticationResponse);
  }

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param request The request
   * @return the jwt token details
   */
  @GetMapping("/refresh-token")
  @Operation(
      summary = "Refresh access token",
      description = "Create and return new access information")
  public AuthenticationResponse refreshToken(HttpServletRequest request) {
    String refreshToken =
        Arrays.stream(request.getCookies())
            .filter(cookie -> TokenType.REFRESH.getName().equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);

    return authService.refreshAccessToken(refreshToken, request);
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  @DeleteMapping("/logout")
  @Operation(summary = "Logout", description = "Logout and clear cookie of user browser")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    HttpHeaders responseHeaders = cookieService.addDeletedCookieToHeaders(TokenType.REFRESH);
    authService.logout(request, response);
    return ResponseEntity.noContent().headers(responseHeaders).build();
  }

  /**
   * Verify account by verification token.
   *
   * @param verificationToken the token
   */
  @GetMapping("/verify/{token}")
  @Operation(summary = "Verify account", description = "Active account by provided token")
  public void verify(@PathVariable(value = "token") String verificationToken) {
    authService.verificationAccount(verificationToken);
  }
}
