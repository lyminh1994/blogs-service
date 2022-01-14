package com.minhlq.blogsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenResponse {

  private final UserPrincipal user;

  private final String token;

  private final String refreshToken;
}
