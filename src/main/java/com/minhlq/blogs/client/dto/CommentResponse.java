package com.minhlq.blogs.client.dto;

import java.util.Date;

public record CommentResponse(
    String id, String body, String articleId, Date createdAt, Date updatedAt, ProfileDTO author) {}
