package com.minhlq.blogs.dto;

import com.minhlq.blogs.annotation.UpdateUserConstraint;
import com.minhlq.blogs.dto.request.UpdateUserRequest;
import com.minhlq.blogs.payload.UserPrincipal;

/**
 * The UpdateUserDto transfers user details from outside into the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@UpdateUserConstraint
public record UpdateUserDto(UserPrincipal targetUser, UpdateUserRequest params) {}
