package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.DuplicatedArticleConstraint;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * This class models the format of the add new article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
public class NewArticleRequest {

  @NotBlank @DuplicatedArticleConstraint private String title;

  @NotBlank private String description;

  @NotBlank private String body;

  private List<String> tagNames;
}
