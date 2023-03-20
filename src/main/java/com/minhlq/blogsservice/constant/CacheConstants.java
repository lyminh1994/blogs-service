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

  /** Constant for the cache key for the roles */
  public static final String ROLE = "role";

  /** Constant for the cache key for the roles */
  public static final String ROLES = "roles";

  /** Constant for the cache key for user details */
  public static final String USER_DETAILS = "userDetails";
}
