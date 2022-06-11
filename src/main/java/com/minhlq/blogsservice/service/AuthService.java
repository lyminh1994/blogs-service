package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.payload.request.LoginRequest;
import com.minhlq.blogsservice.payload.request.RegisterRequest;
import com.minhlq.blogsservice.payload.response.AuthenticationResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
   * Create the user with the register request instance given.
   *
   * @param registerRequest the register information
   * @return the authentication response
   */
  AuthenticationResponse createUser(RegisterRequest registerRequest, HttpHeaders responseHeaders);

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * @param refreshToken current refresh token
   * @param loginRequest login params
   * @param responseHeaders the response headers
   * @return the authentication response
   */
  AuthenticationResponse login(
      String refreshToken, LoginRequest loginRequest, HttpHeaders responseHeaders);

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param refreshToken current refresh token
   * @param request the request
   * @return the authentication response
   */
  AuthenticationResponse refreshToken(String refreshToken, HttpServletRequest request);

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   */
  void logout(HttpServletRequest request, HttpServletResponse response);
}
