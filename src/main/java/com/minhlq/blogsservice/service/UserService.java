package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.AuthenticationResponse;
import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

  AuthenticationResponse createUser(RegisterRequest registerRequest);

  AuthenticationResponse login(LoginRequest loginRequest);

  UserPrincipal updateProfile(@Valid UpdateUserDto updateUserDto);

  ProfileResponse findByUsername(String username, UserPrincipal currentUser);

  ProfileResponse followByUsername(String username, UserPrincipal currentUser);

  ProfileResponse unFollowByUsername(String username, UserPrincipal currentUser);
}
