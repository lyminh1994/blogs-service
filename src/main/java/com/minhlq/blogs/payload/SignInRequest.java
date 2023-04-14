package com.minhlq.blogs.payload;

import jakarta.validation.constraints.NotBlank;

/**
 * This class models the format of the login request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record SignInRequest(
    @NotBlank(message = "{user.username.cannot.blank}") String username,
    @NotBlank(message = "{user.password.cannot.blank}") String password) {}
