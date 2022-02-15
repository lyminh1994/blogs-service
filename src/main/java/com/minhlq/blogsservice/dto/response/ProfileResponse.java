package com.minhlq.blogsservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

  private String username;

  private String bio;

  private String image;

  private boolean following;
}
