package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.validation.DuplicatedArticleConstraint;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewArticleRequest {

  @NotBlank(message = "title can't be empty")
  @DuplicatedArticleConstraint
  private String title;

  @NotBlank(message = "description can't be empty")
  private String description;

  @NotBlank(message = "body can't be empty")
  private String body;

  private List<String> tagNames;
}
