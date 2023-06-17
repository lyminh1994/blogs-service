package com.minhlq.blogs.service;

import com.minhlq.blogs.payload.AuthenticationResponse;
import com.minhlq.blogs.payload.LoginRequest;
import com.minhlq.blogs.payload.RegisterRequest;
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
   * Create a new user account in the system.
   *
   * @param body new account information
   */
  void register(RegisterRequest body);

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * @param refreshToken current refresh token
   * @param body login params
   * @param headers the response headers
   * @return the authentication response
   */
  AuthenticationResponse login(String refreshToken, LoginRequest body, HttpHeaders headers);

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param refreshToken current refresh token
   * @param request the request
   * @return the authentication response
   */
  AuthenticationResponse getAccessToken(String refreshToken, HttpServletRequest request);

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  void logout(HttpServletRequest request, HttpServletResponse response);

  /**
   * Active account by verification token.
   *
   * @param verifyToken the token
   */
  void activeAccount(String verifyToken);
}
