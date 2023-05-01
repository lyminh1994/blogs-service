package com.minhlq.blogs.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class ArticleUtilsTest {

  @Test
  void givenBlankTitle_whenCallingToSlug_thenReturnBlank() {
    String actual = ArticleUtils.toSlug("this*is+my_title");

    assertEquals("this-is-my-title", actual);
  }

  @Test
  void whenCallingToSlug_thenReturnCorrectSlug() {
    String actual = ArticleUtils.toSlug("Title 1");

    assertEquals("title-1", actual);
  }

  @Test
  void whenCallingToSlugWithNullOrBlank_thenReturnEmpty() {
    assertEquals(StringUtils.EMPTY, ArticleUtils.toSlug(null));
    assertEquals(StringUtils.EMPTY, ArticleUtils.toSlug(""));
    assertEquals(StringUtils.EMPTY, ArticleUtils.toSlug(" "));
  }
}
