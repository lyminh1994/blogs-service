package com.minhlq.blogsservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonRootName("user")
public class UserTokenResponse {

  private final UserPrincipal user;

  private final String token;

  private final String refreshToken;

}
