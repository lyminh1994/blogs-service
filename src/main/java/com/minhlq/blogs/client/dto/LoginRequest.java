package com.minhlq.blogs.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record LoginRequest(String email, String password) {}
