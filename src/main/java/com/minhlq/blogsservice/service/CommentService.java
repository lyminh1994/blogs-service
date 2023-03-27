package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.payload.UserPrincipal;
import org.springframework.data.domain.Pageable;

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
   * @param currentUser user details
   * @param slug the slug
   * @param newCommentRequest the comment
   * @return comment
   */
  CommentResponse addCommentToArticle(
      UserPrincipal currentUser, String slug, NewCommentRequest newCommentRequest);

  /**
   * Find all article comments.
   *
   * @param currentUser user details
   * @param slug the slug
   * @param pageable the paging params
   * @return comments
   */
  PageResponse<CommentResponse> findArticleComments(
      UserPrincipal currentUser, String slug, Pageable pageable);

  /**
   * Delete comment of article with provided comment id.
   *
   * @param currentUser user details
   * @param slug the slug
   * @param commentId the comment id
   */
  void deleteCommentFromArticle(UserPrincipal currentUser, String slug, Long commentId);
}
