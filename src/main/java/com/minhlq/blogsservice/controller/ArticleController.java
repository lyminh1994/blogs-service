package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.ArticleResponse;
import com.minhlq.blogsservice.dto.CommentResponse;
import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.service.ArticleService;
import com.minhlq.blogsservice.service.CommentService;
import com.minhlq.blogsservice.util.PagingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles", description = "Article APIs")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  private final CommentService commentService;

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Create article", description = "Create article")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ArticleResponse createArticle(
      @Valid @RequestBody NewArticleRequest articleRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.createArticle(articleRequest, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Get feed", description = "Get current user articles feed")
  @GetMapping("/feed")
  public PagingResponse<ArticleResponse> getFeed(
      @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findUserFeed(
        currentUser, PagingUtils.toPageRequest(pageNumber, pageSize));
  }

  @Operation(summary = "Get articles", description = "Get all articles")
  @GetMapping
  public PagingResponse<ArticleResponse> getArticles(
      @RequestParam(value = "tag", required = false) String tag,
      @RequestParam(value = "favorite_by", required = false) String favoriteBy,
      @RequestParam(value = "author", required = false) String author,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findRecentArticles(
        tag, favoriteBy, author, PageRequest.of(pageNumber, pageSize), currentUser);
  }

  @Operation(summary = "Get article", description = "Get article by slug")
  @GetMapping("/{slug}")
  public ArticleResponse getArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findBySlug(slug, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Update article", description = "Update article")
  @PutMapping("/{slug}")
  public ArticleResponse updateArticle(
      @PathVariable("slug") String slug,
      @Valid @RequestBody UpdateArticleRequest updateArticleRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.updateArticle(slug, updateArticleRequest, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Delete article", description = "Delete article")
  @DeleteMapping("/{slug}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    articleService.deleteArticle(slug, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Favorite article", description = "Favorite article")
  @PostMapping("/{slug}/favorite")
  public ArticleResponse favoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.favoriteArticle(slug, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "UnFavorite article", description = "UnFavorite article")
  @DeleteMapping("/{slug}/favorite")
  public ArticleResponse unFavoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.unFavoriteArticle(slug, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Create comment", description = "Create comment for article")
  @PostMapping("/{slug}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse createComment(
      @PathVariable("slug") String slug,
      @Valid @RequestBody NewCommentRequest newCommentRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return commentService.createComment(slug, newCommentRequest, currentUser);
  }

  @Operation(summary = "Get comments", description = "Get all comments of article")
  @GetMapping("/{slug}/comments")
  public List<CommentResponse> getComments(
      @PathVariable("slug") String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return commentService.findArticleComments(slug, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Delete comment", description = "Delete comment of article")
  @DeleteMapping(value = "/{slug}/comments/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(
      @PathVariable("slug") String slug,
      @PathVariable("id") Long commentId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    commentService.deleteComment(slug, commentId, currentUser);
  }
}
