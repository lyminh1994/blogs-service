package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;

@JsonRootName("article")
public record NewArticleRequest(
    String title, String description, String body, List<String> tagList) {}
