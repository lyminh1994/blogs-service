package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.annotation.DuplicatedArticleConstraint;
import java.util.List;

/**
 * This class models the format of the update article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 2023-08-09
 */
public record UpdateArticleRequest(
    @DuplicatedArticleConstraint String title,
    String body,
    String description,
    List<String> tagNames) {}
