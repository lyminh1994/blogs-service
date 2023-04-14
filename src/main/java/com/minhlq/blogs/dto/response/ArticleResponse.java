package com.minhlq.blogs.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * This class models the format of the article response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public class ArticleResponse {

  private Long id;

  private ProfileResponse author;

  private String slug;

  private String title;

  private String description;

  private String body;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private boolean favorite;

  private long favoritesCount;

  private List<String> tagNames;
}
