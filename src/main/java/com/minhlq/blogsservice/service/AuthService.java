package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.payload.AuthenticationResponse;
import com.minhlq.blogsservice.payload.SignInRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

/**
 * This is the contract for the authentication service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface AuthService {

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * @param refreshToken current refresh token
   * @param requestBody login params
   * @param responseHeaders the response headers
   * @return the authentication response
   */
  AuthenticationResponse signIn(
      String refreshToken, SignInRequest requestBody, HttpHeaders responseHeaders);

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param refreshToken current refresh token
   * @param request the request
   * @return the authentication response
   */
  AuthenticationResponse refreshAccessToken(String refreshToken, HttpServletRequest request);

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  void signOut(HttpServletRequest request, HttpServletResponse response);

  /**
   * Active account by verification token.
   *
   * @param verificationToken the token
   */
  void activeAccount(String verificationToken);
}
