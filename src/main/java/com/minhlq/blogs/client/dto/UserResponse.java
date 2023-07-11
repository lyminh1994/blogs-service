package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record UserResponse(String email, String username, String bio, String image, String token) {}
