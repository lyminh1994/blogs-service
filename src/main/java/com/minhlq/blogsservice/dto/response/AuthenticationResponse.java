package com.minhlq.blogsservice.dto.response;

import com.minhlq.blogsservice.dto.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

  private final UserPrincipal user;

  private final String accessToken;

  private final String refreshToken;
}
