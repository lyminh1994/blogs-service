package com.minhlq.blogs.dto.request;

/**
 * This record is used to represent a request to update a password.
 *
 * @author MinhLy
 * @version 1.0
 * @since 2023-08-09
 */
public record UpdatePasswordRequest(String currentPassword, String newPassword) {}
