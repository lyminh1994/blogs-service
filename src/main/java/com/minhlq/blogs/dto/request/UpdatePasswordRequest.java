package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.annotation.UpdatePasswordConstraint;

/**
 * This record is used to represent a request to update a password.
 *
 * @author MinhLy
 * @version 1.0
 * @since 2023-08-09
 */
public record UpdatePasswordRequest(
    @UpdatePasswordConstraint String currentPassword, String newPassword) {}
