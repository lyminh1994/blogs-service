package com.minhlq.blogsservice.dto.request;

public record UpdatePasswordRequest(String currentPassword, String newPassword) {}
