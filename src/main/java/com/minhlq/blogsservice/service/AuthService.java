package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UserTokenResponse;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;

public interface AuthService {

  UserTokenResponse createUser(RegisterRequest registerRequest);

  UserTokenResponse login(LoginRequest loginRequest);

}
