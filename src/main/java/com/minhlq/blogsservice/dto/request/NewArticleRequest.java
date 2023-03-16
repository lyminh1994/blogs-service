package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.DuplicatedArticleConstraint;
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
    @NotBlank @DuplicatedArticleConstraint String title,
    @NotBlank String description,
    @NotBlank String body,
    List<String> tagNames) {}
