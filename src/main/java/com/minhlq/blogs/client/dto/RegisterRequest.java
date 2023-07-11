package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record RegisterRequest(String email, String username, String password) {}
