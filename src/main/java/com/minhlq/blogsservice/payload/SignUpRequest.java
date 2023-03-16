package com.minhlq.blogsservice.payload;

import static com.minhlq.blogsservice.constant.UserConstants.BLANK_EMAIL;
import static com.minhlq.blogsservice.constant.UserConstants.EMAIL_ALREADY_EXIST;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_EMAIL;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_PASSWORD_SIZE;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_USERNAME_SIZE;
import static com.minhlq.blogsservice.constant.UserConstants.PASSWORD_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_EXISTED;

import com.minhlq.blogsservice.annotation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.annotation.DuplicatedUsernameConstraint;
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
public record SignUpRequest(
    @NotBlank(message = USERNAME_CANNOT_BLANK)
        @Size(min = 3, max = 50, message = INVALID_USERNAME_SIZE)
        @DuplicatedUsernameConstraint(message = USERNAME_EXISTED)
        String username,
    @NotBlank(message = PASSWORD_CANNOT_BLANK) @Size(min = 8, message = INVALID_PASSWORD_SIZE)
        String password,
    @Email(message = INVALID_EMAIL)
        @NotBlank(message = BLANK_EMAIL)
        @DuplicatedEmailConstraint(message = EMAIL_ALREADY_EXIST)
        String email) {}
