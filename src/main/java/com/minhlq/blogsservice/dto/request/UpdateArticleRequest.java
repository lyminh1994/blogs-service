package com.minhlq.blogsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the update article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {

  private String title = "";

  private String body = "";

  private String description = "";
}
