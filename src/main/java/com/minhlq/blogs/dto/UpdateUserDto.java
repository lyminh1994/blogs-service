package com.minhlq.blogs.dto;

import com.minhlq.blogs.dto.request.UpdateUserRequest;
import com.minhlq.blogs.model.UserEntity;

/**
 * The UpdateUserDto transfers user details from outside into the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record UpdateUserDto(UserEntity targetUser, UpdateUserRequest params) {}
