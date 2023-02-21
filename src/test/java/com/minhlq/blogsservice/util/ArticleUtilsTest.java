package com.minhlq.blogsservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleUtilsTest {

  @Test
  void givenBlankTitle_whenCallingToSlug_thenReturnBlank() {
    String actual = ArticleUtils.toSlug("");

    assertEquals("", actual);
  }

  @Test
  void givenTitle_whenCallingToSlug_thenReturnSlug() {
    String actual = ArticleUtils.toSlug("Title 1");

    assertEquals("title-1", actual);
  }
}
