package com.minhlq.blogs.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * This class models the format of the add new comment request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 2023-08-09
 */
public record NewCommentRequest(@NotBlank String body) {}
