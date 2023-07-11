package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record UpdateUserRequest(
    String email, String password, String username, String bio, String image) {}
