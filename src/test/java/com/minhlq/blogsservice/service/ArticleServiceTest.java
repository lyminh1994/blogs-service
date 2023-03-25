package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.repository.ArticleFavoriteRepository;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.ArticleTagRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.impl.ArticleServiceImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @Mock JPAQueryFactory queryFactory;

  @Mock ArticleRepository articleRepository;

  @Mock ArticleTagRepository articleTagRepository;

  @Mock ArticleFavoriteRepository articleFavoriteRepository;

  @Mock TagRepository tagRepository;

  @Mock FollowRepository followRepository;

  @InjectMocks ArticleServiceImpl articleService;

  @Test
  @DisplayName("Should create article success")
  void shouldCreateArticleSuccess() {}

  @Test
  @DisplayName("Should create article with empty tag names success")
  void shouldCreateArticleWithEmptyTagNamesSuccess() {}

  @Test
  @DisplayName("Should create article with existed tag names success")
  void shouldCreateArticleWithExistedTagNamesSuccess() {}

  @Test
  @DisplayName("Should find user feed success")
  void shouldFindUserFeedSuccess() {}

  @Test
  @DisplayName("Should find user feed with empty followed users success")
  void shouldFindUserFeedWithEmptyFollowedUsersSuccess() {}

  @Test
  @DisplayName("Should find recent articles success")
  void shouldFindRecentArticlesSuccess() {}

  @Test
  @DisplayName("Should find recent articles with conditions success")
  void shouldFindRecentArticlesWithConditionsSuccess() {}

  @Test
  @DisplayName("Should find by slug success")
  void shouldFindBySlugSuccess() {}

  @Test
  @DisplayName("Should update article success")
  void shouldUpdateArticleSuccess() {}

  @Test
  @DisplayName("Should update article throw no authorization exception")
  void shouldUpdateArticleThrowNoAuthorizationException() {}

  @Test
  @DisplayName("Should update article throw resource not found exception")
  void shouldUpdateArticleThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should delete article success")
  void shouldDeleteArticleSuccess() {}

  @Test
  @DisplayName("Should delete article throw resource not found exception")
  void shouldDeleteArticleThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should delete article throw no authorization exception")
  void shouldDeleteArticleThrowNoAuthorizationException() {}

  @Test
  @DisplayName("Should favorite article success")
  void shouldFavoriteArticleSuccess() {}

  @Test
  @DisplayName("Should favorite article throw resource not found exception")
  void shouldFavoriteArticleThrowResourceNotFoundException() {}

  @Test
  @DisplayName("Should un-favorite article success")
  void shouldUnFavoriteArticleSuccess() {}

  @Test
  @DisplayName("Should unfavorite article throw resource not found exception")
  void shouldUnFavoriteArticleThrowResourceNotFoundException() {}
}
