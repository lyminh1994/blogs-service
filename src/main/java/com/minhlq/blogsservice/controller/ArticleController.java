package com.minhlq.blogsservice.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.minhlq.blogsservice.constant.AppConstants;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
  public ArticleResponse createArticle(@Valid @RequestBody NewArticleRequest articleRequest) {
    return articleService.createArticle(articleRequest);
  }

  /**
   * Get current user feeds.
   *
   * @param pageNumber page number
   * @param pageSize page size
   * @return paging articles
   */
  @GetMapping(AppConstants.FEEDS)
  @Operation(summary = "Get feed", description = "Get followed user articles")
  public PageResponse<ArticleResponse> getFeeds(
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
    return articleService.findUserFeeds(PageRequest.of(pageNumber, pageSize));
  }

  /**
   * Get all articles by filter params provided.
   *
   * @param tag tag name
   * @param favoriteBy username favorite
   * @param author article author
   * @param pageNumber page number
   * @param pageSize page size
   * @return paging articles
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get articles", description = "Get all user articles")
  public PageResponse<ArticleResponse> getArticles(
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String favoriteBy,
      @RequestParam(required = false) String author,
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
    return articleService.findRecentArticles(
        tag, favoriteBy, author, PageRequest.of(pageNumber, pageSize));
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
  public ArticleResponse getArticle(@PathVariable String slug) {
    return articleService.findBySlug(slug);
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
      @PathVariable String slug, @Valid @RequestBody UpdateArticleRequest updateArticleRequest) {
    return articleService.updateArticle(slug, updateArticleRequest);
  }

  /**
   * Delete article bu slug.
   *
   * @param slug slug
   */
  @DeleteMapping(AppConstants.SLUG)
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Delete article", description = "Delete article by slug")
  public void deleteArticle(@PathVariable String slug) {
    articleService.deleteArticle(slug);
  }

  /**
   * Set favorite article by slug.
   *
   * @param slug slug
   * @return article
   */
  @PutMapping(AppConstants.FAVORITE)
  @Operation(summary = "Favorite article", description = "Favorite article by slug")
  public ArticleResponse favoriteArticle(@PathVariable String slug) {
    return articleService.favoriteArticle(slug);
  }

  /**
   * Delete favorite article by slug.
   *
   * @param slug slug
   * @return article
   */
  @DeleteMapping(AppConstants.FAVORITE)
  @Operation(summary = "UnFavorite article", description = "UnFavorite article by slug")
  public ArticleResponse unFavoriteArticle(@PathVariable String slug) {
    return articleService.unFavoriteArticle(slug);
  }
}
