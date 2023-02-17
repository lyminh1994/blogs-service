package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.UpdatePasswordConstraint;

public record UpdatePasswordRequest(@UpdatePasswordConstraint String currentPassword, String newPassword) {}
