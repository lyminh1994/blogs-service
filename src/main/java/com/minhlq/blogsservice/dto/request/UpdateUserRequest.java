package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.validation.DuplicatedEmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

  @Builder.Default
  @Email(message = "should be an email")
  @DuplicatedEmailConstraint(message = "email already exist")
  private final String email = "";

  @Builder.Default
  private final String password = "";

  @Builder.Default
  private final String bio = "";

  @Builder.Default
  private final String image = "";

}
