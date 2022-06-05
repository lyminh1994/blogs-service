package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants used in the Cache Maps as keys to the values.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstants {

  // User cache
  public static final String USERS = "users";
  public static final String USER_DETAILS = "userDetails";

  // Weather API cache
  public static final String CURRENT_WEATHER = "currentWeather";
}
