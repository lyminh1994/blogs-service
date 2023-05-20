package com.minhlq.blogs.service;

import com.minhlq.blogs.dto.request.NewArticleRequest;
import com.minhlq.blogs.dto.request.UpdateArticleRequest;
import com.minhlq.blogs.dto.response.ArticleResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.payload.UserPrincipal;
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
   * @param currentUser user details
   * @param createRequest the article content
   * @return article
   */
  ArticleResponse createArticle(UserPrincipal currentUser, NewArticleRequest createRequest);

  /**
   * Find all articles created by current user.
   *
   * @param currentUser user details
   * @param pageable the paging params
   * @return the paging articles
   */
  PageResponse<ArticleResponse> findUserFeeds(UserPrincipal currentUser, Pageable pageable);

  /**
   * Find all articles with provided filter params.
   *
   * @param currentUser user details
   * @param tagName the tag name
   * @param favoriteBy the favorite user
   * @param author the article author
   * @param pageable the paging params
   * @return the paging articles
   */
  PageResponse<ArticleResponse> findRecentArticles(
      UserPrincipal currentUser,
      String tagName,
      String favoriteBy,
      String author,
      Pageable pageable);

  /**
   * Find article by provided slug.
   *
   * @param currentUser user details
   * @param slug the slug
   * @return article
   */
  ArticleResponse findBySlug(UserPrincipal currentUser, String slug);

  /**
   * Update article by slug with provided information.
   *
   * @param currentUser user details
   * @param slug the slug
   * @param updateRequest update fields
   * @return article
   */
  ArticleResponse updateArticle(
      UserPrincipal currentUser, String slug, UpdateArticleRequest updateRequest);

  /**
   * Delete single article by slug.
   *
   * @param currentUser user details
   * @param slug the slug
   */
  void deleteArticle(UserPrincipal currentUser, String slug);

  /**
   * Set favorite article by current user.
   *
   * @param currentUser user details
   * @param slug the slug
   * @return article
   */
  ArticleResponse favoriteArticle(UserPrincipal currentUser, String slug);

  /**
   * Un-favorite article by current user.
   *
   * @param currentUser user details
   * @param slug the slug
   * @return article
   */
  ArticleResponse unFavoriteArticle(UserPrincipal currentUser, String slug);

  /**
   * Check article slug has exited in table articles
   *
   * @param slug article slug
   * @return is exited
   */
  boolean isSlugExited(String slug);
}