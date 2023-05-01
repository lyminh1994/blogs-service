package com.minhlq.blogs.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Utilities class handle operation for article.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public final class ArticleUtils {

  private final String REGEX_SPECIAL_CHARACTERS = "[^A-Za-z0-9]+";

  /**
   * Replace special characters and transfer to lowercase.
   *
   * @param title the article slug
   * @return formatted slug
   */
  public String toSlug(String title) {
    if (StringUtils.isBlank(title)) {
      return StringUtils.EMPTY;
    }

    return title.toLowerCase().replaceAll(REGEX_SPECIAL_CHARACTERS, "-");
  }
}
