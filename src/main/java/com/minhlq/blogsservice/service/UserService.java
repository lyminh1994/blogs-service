package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.response.AuthenticationResponse;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

  AuthenticationResponse createUser(RegisterRequest registerRequest);

  AuthenticationResponse login(LoginRequest loginRequest);

  UserPrincipal updateProfile(@Valid UpdateUserDto updateUserDto);

  ProfileResponse findByUsername(String username);

  ProfileResponse followByUsername(String username);

  ProfileResponse unFollowByUsername(String username);
}
