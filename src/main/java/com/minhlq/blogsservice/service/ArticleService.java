package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import org.springframework.data.domain.PageRequest;

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
     * @param pageRequest the paging params
     * @return the paging articles
     */
    PageResponse<ArticleResponse> findUserFeeds(PageRequest pageRequest);

    /**
     * Find all articles with provided filter params.
     *
     * @param tagName     the tag name
     * @param favoriteBy  the favorite user
     * @param author      the article author
     * @param pageRequest the paging params
     * @return the paging articles
     */
    PageResponse<ArticleResponse> findRecentArticles(
            String tagName, String favoriteBy, String author, PageRequest pageRequest);

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
     * @param slug          the slug
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

    /**
     * Check article slug has exited in table article *
     *
     * @param slug article slug
     * @return is exited
     */
    boolean isSlugExited(String slug);
}
