package com.minhlq.blogs.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * This class models the format of the comment response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public class CommentResponse {

  private String id;

  private String body;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private ProfileResponse user;
}
