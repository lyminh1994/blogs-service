package com.minhlq.blogs.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled("not ready yet")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Test
  void should_create_comment_success() {}

  @Test
  void should_create_comment_throw_resource_not_found_exception_when_article_not_exist() {}

  @Test
  void should_find_article_comments_success() {}

  @Test
  void should_find_article_comments_throw_resource_not_found_exception_when_article_not_exist() {}

  @Test
  void should_delete_comment_success() {}

  @Test
  void should_delete_comment_throw_resource_not_found_exception_when_article_not_exist() {}

  @Test
  void should_delete_comment_throw_resource_not_found_exception_when_comment_not_exist() {}

  @Test
  void should_delete_comment_throw_no_authorization_exception() {}
}
