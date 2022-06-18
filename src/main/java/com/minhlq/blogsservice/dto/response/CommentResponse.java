package com.minhlq.blogsservice.dto.response;

import java.time.Instant;
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

  private Instant createdAt;

  private Instant updatedAt;

  private ProfileResponse user;
}
