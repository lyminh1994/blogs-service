package com.minhlq.blogsservice.dto;

import com.minhlq.blogsservice.annotation.UpdateUserConstraint;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.payload.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The UpdateUserDto transfers user details from outside into the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@UpdateUserConstraint
public class UpdateUserDto {

    private final UserPrincipal targetUser;

    private final UpdateUserRequest params;
}
