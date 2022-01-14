package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.ArticleResponse;
import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import org.springframework.data.domain.PageRequest;

public interface ArticleService {

  ArticleResponse createArticle(NewArticleRequest createRequest, UserPrincipal currentUser);

  PagingResponse<ArticleResponse> findUserFeed(UserPrincipal currentUser, PageRequest pageRequest);

  PagingResponse<ArticleResponse> findRecentArticles(
      String tag,
      String favoriteBy,
      String author,
      PageRequest pageRequest,
      UserPrincipal currentUser);

  ArticleResponse findBySlug(String slug, UserPrincipal currentUser);

  ArticleResponse updateArticle(
      String slug, UpdateArticleRequest updateRequest, UserPrincipal currentUser);

  void deleteArticle(String slug, UserPrincipal currentUser);

  ArticleResponse favoriteArticle(String slug, UserPrincipal currentUser);

  ArticleResponse unFavoriteArticle(String slug, UserPrincipal currentUser);
}
