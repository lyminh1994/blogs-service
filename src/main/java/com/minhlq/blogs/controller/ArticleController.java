package com.minhlq.blogs.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.dto.request.NewArticleRequest;
import com.minhlq.blogs.dto.request.UpdateArticleRequest;
import com.minhlq.blogs.dto.response.ArticleResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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

/**
 * This controller handles all requests relating to articles.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.ARTICLES)
@Tag(name = "Articles", description = "Blog Article APIs")
public class ArticleController {

  private final ArticleService articleService;

  /**
   * Create article with article information provided.
   *
   * @param articleRequest new article request
   * @return the article
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create article", description = "Create article")
  public ArticleResponse createArticle(
      @RequestBody @Valid NewArticleRequest articleRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.createArticle(currentUser, articleRequest);
  }

  /**
   * Get current user feeds.
   *
   * @param pageable paging
   * @return paging articles
   */
  @GetMapping(AppConstants.FEEDS)
  @Operation(summary = "Get feed", description = "Get followed user articles")
  public PageResponse<ArticleResponse> getFeeds(
      @ParameterObject Pageable pageable, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findUserFeeds(currentUser, pageable);
  }

  /**
   * Get all articles by filter params provided.
   *
   * @param tag tag name
   * @param favoriteBy username favorite
   * @param author article author
   * @param pageable paging
   * @return paging articles
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get articles", description = "Get all user articles")
  public PageResponse<ArticleResponse> getArticles(
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String favoriteBy,
      @RequestParam(required = false) String author,
      @ParameterObject Pageable pageable,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findRecentArticles(currentUser, tag, favoriteBy, author, pageable);
  }

  /**
   * Get article by slug.
   *
   * @param slug slug
   * @return article
   */
  @SecurityRequirements
  @GetMapping(AppConstants.SLUG)
  @Operation(summary = "Get article", description = "Get article by slug")
  public ArticleResponse getArticle(
      @PathVariable String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.findBySlug(currentUser, slug);
  }

  /**
   * Update article with slug and details provided.
   *
   * @param slug slug
   * @param updateArticleRequest update article details
   * @return updated article
   */
  @PutMapping(AppConstants.SLUG)
  @Operation(summary = "Update article", description = "Update article by slug")
  public ArticleResponse updateArticle(
      @PathVariable String slug,
      @RequestBody @Valid UpdateArticleRequest updateArticleRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.updateArticle(currentUser, slug, updateArticleRequest);
  }

  /**
   * Delete article bu slug.
   *
   * @param slug slug
   */
  @DeleteMapping(AppConstants.SLUG)
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Delete article", description = "Delete article by slug")
  public void deleteArticle(
      @PathVariable String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    articleService.deleteArticle(currentUser, slug);
  }

  /**
   * Set favorite article by slug.
   *
   * @param slug slug
   * @return article
   */
  @PutMapping(AppConstants.FAVORITE)
  @Operation(summary = "Favorite article", description = "Favorite article by slug")
  public ArticleResponse favoriteArticle(
      @PathVariable String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.favoriteArticle(currentUser, slug);
  }

  /**
   * Delete favorite article by slug.
   *
   * @param slug slug
   * @return article
   */
  @DeleteMapping(AppConstants.FAVORITE)
  @Operation(summary = "UnFavorite article", description = "UnFavorite article by slug")
  public ArticleResponse unFavoriteArticle(
      @PathVariable String slug, @AuthenticationPrincipal UserPrincipal currentUser) {
    return articleService.unFavoriteArticle(currentUser, slug);
  }
}
