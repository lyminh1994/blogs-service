package com.minhlq.blogsservice.payload;

import jakarta.validation.constraints.NotBlank;

import static com.minhlq.blogsservice.constant.UserConstants.PASSWORD_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_CANNOT_BLANK;

/**
 * This class models the format of the login request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record SignInRequest(
    @NotBlank(message = USERNAME_CANNOT_BLANK) String username,
    @NotBlank(message = PASSWORD_CANNOT_BLANK) String password) {}
