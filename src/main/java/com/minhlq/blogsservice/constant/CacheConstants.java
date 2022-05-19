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

  public static final String USERS = "users";

  public static final String USER_DETAILS = "userDetails";

  public static final String JWT_PREFIX = "security:jwt:";
}
