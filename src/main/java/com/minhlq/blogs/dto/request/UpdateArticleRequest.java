package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.annotation.DuplicatedArticleConstraint;
import jakarta.validation.constraints.Max;
import java.util.List;

/**
 * This class models the format of the update article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record UpdateArticleRequest(
    @Max(255) @DuplicatedArticleConstraint String title,
    @Max(255) String body,
    @Max(255) String description,
    List<String> tagNames) {}
