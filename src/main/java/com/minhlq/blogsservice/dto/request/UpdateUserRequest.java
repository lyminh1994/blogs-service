package com.minhlq.blogsservice.dto.request;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

  @Builder.Default
  @Email(message = "should be an email")
  private final String email = "";

  @Builder.Default private final String bio = "";

  @Builder.Default private final String image = "";
}
