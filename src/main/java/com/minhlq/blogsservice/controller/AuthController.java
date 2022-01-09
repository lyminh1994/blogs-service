package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.UserTokenResponse;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Register", description = "Register new account")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserTokenResponse createUser(@Valid @RequestBody RegisterRequest registerRequest) {
    return authService.createUser(registerRequest);
  }

  @Operation(summary = "Login", description = "Login into system")
  @PostMapping("/login")
  public UserTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

}
