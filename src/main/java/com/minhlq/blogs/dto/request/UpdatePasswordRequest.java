package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.annotation.UpdatePasswordConstraint;

public record UpdatePasswordRequest(@UpdatePasswordConstraint String currentPassword, String newPassword) {}
