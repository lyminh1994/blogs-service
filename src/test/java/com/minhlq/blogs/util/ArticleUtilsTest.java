package com.minhlq.blogs.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleUtilsTest {

  @Test
  @DisplayName("Successful toSlug call")
  void whenCallingToSlug_thenReturnCorrectSlug() {
    String actual = ArticleUtils.toSlug("This is a test Title!!   %##");

    assertEquals("this-is-a-test-title", actual);
  }

  @Test
  @DisplayName("Call toSlug with empty or space title")
  void givenBlankTitle_whenCallingToSlug_thenReturnEmpty() {
    String actualEmpty = ArticleUtils.toSlug(StringUtils.EMPTY);
    String actualSpace = ArticleUtils.toSlug(StringUtils.SPACE);

    assertAll(
        () -> {
          assertEquals(StringUtils.EMPTY, actualEmpty);
          assertEquals(StringUtils.EMPTY, actualSpace);
        });
  }

  @Test
  @DisplayName("Call toSlug with null title")
  void whenCallingToSlugWithNull_thenReturnEmpty() {
    String actual = ArticleUtils.toSlug(null);

    assertEquals(StringUtils.EMPTY, actual);
  }
}
