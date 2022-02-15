package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PagingResponse;
import org.springframework.data.domain.PageRequest;

public interface ArticleService {

  ArticleResponse createArticle(NewArticleRequest createRequest);

  PagingResponse<ArticleResponse> findUserFeed(PageRequest pageRequest);

  PagingResponse<ArticleResponse> findRecentArticles(
      String tagName, String favoriteBy, String author, PageRequest pageRequest);

  ArticleResponse findBySlug(String slug);

  ArticleResponse updateArticle(String slug, UpdateArticleRequest updateRequest);

  void deleteArticle(String slug);

  ArticleResponse favoriteArticle(String slug);

  ArticleResponse unFavoriteArticle(String slug);
}