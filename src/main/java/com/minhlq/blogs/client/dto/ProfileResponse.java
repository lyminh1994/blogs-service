package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("profile")
public record ProfileResponse(
    String id, String username, String bio, String image, boolean following) {}
