package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.helper.BaseControllerTest;
import com.minhlq.blogsservice.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends BaseControllerTest {
  @Autowired MockMvc mockMvc;
  @MockBean CommentService commentService;

  @Test
  @DisplayName("Should Create Comment Success POST request to endpoint - /articles/{slug}/comments")
  void shouldCreateCommentSuccess() {}

  @Test
  @DisplayName(
      "Should Get 422 With Empty Body POST request to endpoint - /articles/{slug}/comments")
  void shouldGet422WithEmptyBody() {}

  @Test
  @DisplayName(
      "Should Get Comments Of Article Success GET request to endpoint - /articles/{slug}/comments")
  void shouldGetCommentsOfArticleSuccess() {}

  @Test
  @DisplayName(
      "Should Delete Comment Success DELETE request to endpoint - /articles/{slug}/comments/{id}")
  void shouldDeleteCommentSuccess() {}

  @Test
  @DisplayName(
      "Should Get 403 If Not Author Of Article Or Author Of Comment "
          + "When Delete Comment DELETE request to endpoint - /articles/{slug}/comments/{id}")
  void shouldGet403IfNotAuthorOfArticleOrAuthorOfCommentWhenDeleteComment() {}
}
