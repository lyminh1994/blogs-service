package com.minhlq.blogs.client.dto;

import java.util.Date;
import java.util.List;

public record ArticleResponse(
    String id,
    String slug,
    String title,
    String body,
    boolean favorited,
    int favoritesCount,
    Date createdAt,
    Date updatedAt,
    List<String> tagList,
    ProfileDTO author) {}
