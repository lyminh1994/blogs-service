package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.annotation.DuplicatedArticleConstraint;

/**
 * This class models the format of the update article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record UpdateArticleRequest(
    @DuplicatedArticleConstraint String title, String body, String description) {}
