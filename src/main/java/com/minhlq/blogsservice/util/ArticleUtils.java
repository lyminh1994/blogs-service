package com.minhlq.blogsservice.util;

import lombok.experimental.UtilityClass;

/**
 * Utilities class handle operation for article.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public class ArticleUtils {

  private static final String REGEX_SPECIAL_CHARACTERS = "[&\\uFE30-\\uFFA0’”\\s?,.]+";

  /**
   * Replace special characters and transfer to lowercase.
   *
   * @param title the article slug
   * @return formatted slug
   */
  public String toSlug(String title) {
    return title.toLowerCase().replaceAll(REGEX_SPECIAL_CHARACTERS, "-");
  }
}
