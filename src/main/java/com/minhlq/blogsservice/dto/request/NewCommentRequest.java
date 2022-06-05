package com.minhlq.blogsservice.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the add new comment request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

  @NotBlank(message = "body can't be empty")
  private String body;
}
