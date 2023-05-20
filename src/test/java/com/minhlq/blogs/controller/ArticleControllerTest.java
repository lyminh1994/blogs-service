package com.minhlq.blogs.controller;

import com.minhlq.blogs.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean ArticleService articleService;

  @Test
  @DisplayName("Should Create Article Success POST request to endpoint - /articles")
  void shouldCreateArticleSuccess() {}

  @Test
  @DisplayName("Should Get Error Message With Wrong Parameter POST request to endpoint - /articles")
  void shouldGetErrorMessageWithWrongParameter() {}

  @Test
  @DisplayName(
      "Should Get Error Message With Duplicated Title POST request to endpoint - /articles")
  void shouldGetErrorMessageWithDuplicatedTitle() {}

  @Test
  @DisplayName("Should Get Feeds 401 Without Login GET request to endpoint - /articles/feed")
  void shouldGetFeeds401WithoutLogin() {}

  @Test
  @DisplayName("Should Get Feeds Success GET request to endpoint - /articles/feed")
  void shouldGetFeedsSuccess() {}

  @Test
  @DisplayName("Should Get Default Article List GET request to endpoint - /articles")
  void shouldGetDefaultArticleList() {}

  @Test
  @DisplayName("Should Read Article Success GET request to endpoint - /articles/{slug}")
  void shouldReadArticleSuccess() {}

  @Test
  @DisplayName("Should 404 If Article Not Found GET request to endpoint - /articles/{slug}")
  void should404IfArticleNotFound() {}

  @Test
  @DisplayName(
      "Should Update Article Content Success UPDATE request to endpoint - /articles/{slug}")
  void shouldUpdateArticleContentSuccess() {}

  @Test
  @DisplayName(
      "Should Get 403 If Not Author To Update Article UPDATE request to endpoint - /articles/{slug}")
  void shouldGet403IfNotAuthorToUpdateArticle() {}

  @Test
  @DisplayName("Should Delete Article Success DELETE request to endpoint - /articles/{slug}")
  void shouldDeleteArticleSuccess() {}

  @Test
  @DisplayName(
      "Should 403 If Not Author Delete Article DELETE request to endpoint - /articles/{slug}")
  void should403IfNotAuthorDeleteArticle() {}

  @Test
  @DisplayName(
      "Should Favorite An Article Success POST request to endpoint - /articles/{slug}/favorite")
  void shouldFavoriteAnArticleSuccess() {}

  @Test
  @DisplayName(
      "Should Un Favorite An Article Success DELETE request to endpoint - /articles/{slug}/favorite")
  void shouldUnFavoriteAnArticleSuccess() {}
}
