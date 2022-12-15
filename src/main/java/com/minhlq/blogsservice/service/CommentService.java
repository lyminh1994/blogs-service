package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import org.springframework.data.domain.PageRequest;

/**
 * The comment service to provide for the comment operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface CommentService {

    /**
     * Add new comment to article.
     *
     * @param slug              the slug
     * @param newCommentRequest the comment
     * @return comment
     */
    CommentResponse addCommentToArticle(String slug, NewCommentRequest newCommentRequest);

    /**
     * Find all article comments.
     *
     * @param slug        the slug
     * @param pageRequest the paging params
     * @return comments
     */
    PageResponse<CommentResponse> findArticleComments(String slug, PageRequest pageRequest);

    /**
     * Delete comment of article with provided comment id.
     *
     * @param slug      the slug
     * @param commentId the comment id
     */
    void deleteCommentFromArticle(String slug, Long commentId);
}
