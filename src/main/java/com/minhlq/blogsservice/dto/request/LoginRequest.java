package com.minhlq.blogsservice.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "can't be empty")
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;
}
