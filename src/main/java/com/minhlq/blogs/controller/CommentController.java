package com.minhlq.blogs.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.dto.request.NewCommentRequest;
import com.minhlq.blogs.dto.response.CommentResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles all requests relating to article comments.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.COMMENTS_ENDPOINT)
@Tag(name = "Comments", description = "Blog Comments of Article APIs")
public class CommentController {

  private final CommentService commentService;

  /**
   * Add comment to article by slug.
   *
   * @param slug slug
   * @param newCommentRequest comment details
   * @return comment
   */
  @PostMapping
  @ResponseStatus(CREATED)
  @Operation(summary = "Create comment", description = "Create comment for article")
  public CommentResponse createComment(
      @PathVariable String slug, @RequestBody @Valid NewCommentRequest newCommentRequest) {
    return commentService.addCommentToArticle(slug, newCommentRequest);
  }

  /**
   * Get all comments of article by slug.
   *
   * @param slug slug
   * @param pageable paging
   * @return comments
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get comments", description = "Get all comments for article by slug")
  public PageResponse<CommentResponse> getComments(
      @PathVariable String slug, @ParameterObject Pageable pageable) {
    return commentService.findArticleComments(slug, pageable);
  }

  /**
   * Delete comment of article.
   *
   * @param slug slug
   * @param commentId id
   */
  @DeleteMapping(AppConstants.COMMENT_ENDPOINT)
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Delete comment", description = "Delete comment of article")
  public void deleteComment(@PathVariable String slug, @PathVariable Long commentId) {
    commentService.deleteCommentFromArticle(slug, commentId);
  }
}
