package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/articles/{slug}/comments")
@Tag(name = "Comments", description = "Article Comments APIs")
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
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create comment", description = "Create comment for article")
  public CommentResponse createComment(
      @PathVariable("slug") String slug, @Valid @RequestBody NewCommentRequest newCommentRequest) {
    return commentService.createComment(slug, newCommentRequest);
  }

  /**
   * Get all comments of article by slug.
   *
   * @param slug slug
   * @return comments
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get comments", description = "Get all comments by article slug")
  public List<CommentResponse> getComments(@PathVariable("slug") String slug) {
    return commentService.findArticleComments(slug);
  }

  /**
   * Delete comment of article.
   *
   * @param slug slug
   * @param commentId id
   */
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete comment", description = "Delete comment of article")
  public void deleteComment(@PathVariable("slug") String slug, @PathVariable("id") Long commentId) {
    commentService.deleteComment(slug, commentId);
  }
}
