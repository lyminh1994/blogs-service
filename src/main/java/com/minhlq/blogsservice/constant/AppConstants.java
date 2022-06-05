package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all constants used by the whole application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

  public static final String DEFAULT_PAGE_NUMBER = "0";
  public static final String DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_SORT_BY = "id";
  public static final String DEFAULT_SORT_DIRECTION = "asc";
}
