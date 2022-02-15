package com.minhlq.blogsservice.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

  private String id;

  private String body;

  private Instant createdAt;

  private Instant updatedAt;

  private ProfileResponse user;
}
