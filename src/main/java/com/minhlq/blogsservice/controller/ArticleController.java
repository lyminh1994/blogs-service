package com.minhlq.blogsservice.controller;

import static com.minhlq.blogsservice.utils.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.minhlq.blogsservice.utils.AppConstants.DEFAULT_PAGE_SIZE;

import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles", description = "Article APIs")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Create article", description = "Create article")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ArticleResponse createArticle(@Valid @RequestBody NewArticleRequest articleRequest) {
    return articleService.createArticle(articleRequest);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Get feeds", description = "Get current user articles feeds")
  @GetMapping("/feeds")
  public PageResponse<ArticleResponse> getFeeds(
      @RequestParam(value = "page-number", required = false, defaultValue = DEFAULT_PAGE_NUMBER)
          int pageNumber,
      @RequestParam(value = "page-size", required = false, defaultValue = DEFAULT_PAGE_SIZE)
          int pageSize) {
    return articleService.findUserFeeds(PageRequest.of(pageNumber, pageSize));
  }

  @Operation(summary = "Get articles", description = "Get all articles")
  @GetMapping
  public PageResponse<ArticleResponse> getArticles(
      @RequestParam(value = "tag", required = false) String tagName,
      @RequestParam(value = "favorite-by", required = false) String favoriteBy,
      @RequestParam(value = "author", required = false) String author,
      @RequestParam(value = "page-number", required = false, defaultValue = DEFAULT_PAGE_NUMBER)
          int pageNumber,
      @RequestParam(value = "page-size", required = false, defaultValue = DEFAULT_PAGE_SIZE)
          int pageSize) {
    return articleService.findRecentArticles(
        tagName, favoriteBy, author, PageRequest.of(pageNumber, pageSize));
  }

  @Operation(summary = "Get article", description = "Get article by slug")
  @GetMapping("/{slug}")
  public ArticleResponse getArticle(@PathVariable("slug") String slug) {
    return articleService.findBySlug(slug);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Update article", description = "Update article by slug")
  @PutMapping("/{slug}")
  public ArticleResponse updateArticle(
      @PathVariable("slug") String slug,
      @Valid @RequestBody UpdateArticleRequest updateArticleRequest) {
    return articleService.updateArticle(slug, updateArticleRequest);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Delete article", description = "Delete article by slug")
  @DeleteMapping("/{slug}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteArticle(@PathVariable("slug") String slug) {
    articleService.deleteArticle(slug);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Favorite article", description = "Favorite article by slug")
  @PostMapping("/favorite/{slug}")
  public ArticleResponse favoriteArticle(@PathVariable("slug") String slug) {
    return articleService.favoriteArticle(slug);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "UnFavorite article", description = "UnFavorite article by slug")
  @DeleteMapping("/favorite/{slug}")
  public ArticleResponse unFavoriteArticle(@PathVariable("slug") String slug) {
    return articleService.unFavoriteArticle(slug);
  }
}
