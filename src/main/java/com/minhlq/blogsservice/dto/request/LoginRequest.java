package com.minhlq.blogsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "can't be empty")
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;

}
