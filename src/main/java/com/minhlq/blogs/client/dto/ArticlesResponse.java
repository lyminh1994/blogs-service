package com.minhlq.blogs.client.dto;

import java.util.List;

public record ArticlesResponse(List<ArticleResponse> articles, int articlesCount) {}
