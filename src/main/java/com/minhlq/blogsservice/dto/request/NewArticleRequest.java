package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.DuplicatedArticleConstraint;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the add new article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewArticleRequest {

  @NotBlank(message = "Title can't be empty")
  @DuplicatedArticleConstraint
  private String title;

  @NotBlank(message = "Description can't be empty")
  private String description;

  @NotBlank(message = "Body can't be empty")
  private String body;

  private List<String> tagNames;
}
