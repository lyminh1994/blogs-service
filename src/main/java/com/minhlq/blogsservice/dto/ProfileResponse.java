package com.minhlq.blogsservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

  @JsonIgnore
  private String id;

  private String username;

  private String bio;

  private String image;

  private boolean following;

}
