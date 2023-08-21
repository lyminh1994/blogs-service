package com.minhlq.blogs.dto.request;

import java.util.List;

/**
 * This class models the format of the update article request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 2023-08-09
 */
public record UpdateArticleRequest(
    String title, String body, String description, List<String> tagNames) {}
