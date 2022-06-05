package com.minhlq.blogsservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArticleUtils {

  private static final String REGEX_SPECIAL_CHARACTERS = "[&\\uFE30-\\uFFA0’”\\s?,.]+";

  public String toSlug(String title) {
    return title.toLowerCase().replaceAll(REGEX_SPECIAL_CHARACTERS, "-");
  }
}
