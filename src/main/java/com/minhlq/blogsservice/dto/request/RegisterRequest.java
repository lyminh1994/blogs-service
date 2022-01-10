package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.validation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.validation.DuplicatedUsernameConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "can't be empty")
  @Email(message = "should be an email")
  @DuplicatedEmailConstraint
  private String email;

  @NotBlank(message = "can't be empty")
  @DuplicatedUsernameConstraint
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;

}
