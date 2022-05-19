package com.minhlq.blogsservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArticleUtilsTest {

  @Test
  @DisplayName("Should to slug success")
  void shouldToSlugSuccess() {
    String actual = ArticleUtils.toSlug("title 1");

    assertEquals("title-1", actual);
  }
}
