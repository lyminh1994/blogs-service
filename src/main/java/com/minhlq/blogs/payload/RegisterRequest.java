package com.minhlq.blogs.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * This class models the format of the register request allowed through the controller endpoints.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record RegisterRequest(
    @NotBlank(message = "{user.username.cannot.blank}")
        @Size(min = 3, max = 50, message = "{user.invalid.username.size}")
        String username,
    @NotBlank(message = "{user.password.cannot.blank}")
        @Size(min = 8, message = "{user.invalid.password.size}")
        String password,
    @Email(message = "{user.invalid.email}")
        @NotBlank(message = "{user.blank.email}")
        String email) {}
