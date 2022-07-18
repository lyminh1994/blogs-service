package com.minhlq.blogsservice.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * This class models the format of the add new comment request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
public class NewCommentRequest {

  @NotBlank(message = "Body can't be empty")
  private String body;
}
