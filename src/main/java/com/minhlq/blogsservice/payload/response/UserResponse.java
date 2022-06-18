package com.minhlq.blogsservice.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class UserResponse {

  private Long id;

  private String username;

  private String email;

  private String bio;

  private String image;
}
