package com.minhlq.blogsservice.dto;

import com.minhlq.blogsservice.model.Tag;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {

  private Long id;

  private ProfileResponse author;

  private String slug;

  private String title;

  private String description;

  private String body;

  private Instant createdAt;

  private Instant updatedAt;

  private boolean favorite;

  private int favoritesCount;

  private List<Tag> tags;
}
