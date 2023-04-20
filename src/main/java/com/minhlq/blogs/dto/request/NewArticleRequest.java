package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.annotation.DuplicatedArticleConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * This class models the format of the add new article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record NewArticleRequest(
    @NotBlank @Max(255) @DuplicatedArticleConstraint String title,
    @NotBlank @Max(255) String description,
    @NotBlank @Max(255) String body,
    List<String> tagNames) {}
