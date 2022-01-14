package com.minhlq.blogsservice.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

  @NotBlank(message = "body can't be empty")
  private String body;
}
