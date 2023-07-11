package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
public record NewCommentRequest(String body) {}
