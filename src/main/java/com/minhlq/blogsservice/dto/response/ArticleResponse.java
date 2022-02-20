package com.minhlq.blogsservice.dto.response;

import java.util.Date;
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

  private Date createdAt;

  private Date updatedAt;

  private boolean favorite;

  private long favoritesCount;

  private List<String> tagNames;
}
