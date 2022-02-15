package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.response.AuthenticationResponse;
import com.minhlq.blogsservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @Operation(summary = "Register", description = "Register new account")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthenticationResponse createUser(@Valid @RequestBody RegisterRequest registerRequest) {
    return userService.createUser(registerRequest);
  }

  @Operation(summary = "Login", description = "Login into system")
  @PostMapping("/login")
  public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    return userService.login(loginRequest);
  }
}
