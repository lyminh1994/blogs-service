package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("article")
public record UpdateArticleReq(String title, String body, String description) {}
