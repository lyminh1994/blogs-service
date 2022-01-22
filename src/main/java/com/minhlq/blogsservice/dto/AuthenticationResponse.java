package com.minhlq.blogsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

  private final UserPrincipal user;

  private final String accessToken;

  private final String refreshToken;
}
