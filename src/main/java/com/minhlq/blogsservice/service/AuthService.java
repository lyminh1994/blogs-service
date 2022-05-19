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

  AuthenticationResponse createUser(RegisterRequest registerRequest, HttpHeaders responseHeaders);

  AuthenticationResponse login(
      String refreshToken, LoginRequest loginRequest, HttpHeaders responseHeaders);

  AuthenticationResponse refreshToken(
      String refreshToken, HttpServletRequest request, HttpHeaders responseHeaders);

  void logout(HttpServletRequest request, HttpServletResponse response);
}
