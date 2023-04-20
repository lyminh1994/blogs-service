package com.minhlq.blogs.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

/**
 * This class models the format of the add new comment request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record NewCommentRequest(@NotBlank @Max(255) String body) {}
