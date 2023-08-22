package com.minhlq.blogs.service;

import com.minhlq.blogs.dto.request.NewArticleRequest;
import com.minhlq.blogs.dto.request.UpdateArticleRequest;
import com.minhlq.blogs.dto.response.ArticleResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * The article service to provide for the article operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface ArticleService {

  /**
   * Crete a new article with provided content.
   *
   * @param createRequest the article content
   * @return article
   */
  ArticleResponse createArticle(NewArticleRequest createRequest);

  /**
   * Find all articles created by current user.
   *
   * @param pageable the paging params
   * @return the paging articles
   */
  PageResponse<ArticleResponse> findUserFeeds(Pageable pageable);

  /**
   * Find all articles with provided filter params.
   *
   * @param tagName the tag name
   * @param favoriteBy the favorite user
   * @param author the article author
   * @param pageable the paging params
   * @return the paging articles
   */
  PageResponse<ArticleResponse> findRecentArticles(
      String tagName, String favoriteBy, String author, Pageable pageable);

  /**
   * Find article by provided slug.
   *
   * @param slug the slug
   * @return article
   */
  ArticleResponse findBySlug(String slug);

  /**
   * Update article by slug with provided information.
   *
   * @param slug the slug
   * @param updateRequest update fields
   * @return article
   */
  ArticleResponse updateArticle(String slug, UpdateArticleRequest updateRequest);

  /**
   * Delete single article by slug.
   *
   * @param slug the slug
   */
  void deleteArticle(String slug);

  /**
   * Set favorite article by current user.
   *
   * @param slug the slug
   * @return article
   */
  ArticleResponse favoriteArticle(String slug);

  /**
   * Un-favorite article by current user.
   *
   * @param slug the slug
   * @return article
   */
  ArticleResponse unFavoriteArticle(String slug);
}
