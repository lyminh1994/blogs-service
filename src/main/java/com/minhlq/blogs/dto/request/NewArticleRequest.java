package com.minhlq.blogs.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * This class models the format of the add new article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 2023-08-09
 */
public record NewArticleRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String body,
    List<String> tagNames) {}
