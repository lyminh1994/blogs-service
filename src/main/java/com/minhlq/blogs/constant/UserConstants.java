package com.minhlq.blogs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * User constant provides details about user.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConstants {

  public static final int DAYS_TO_ALLOW_ACCOUNT_ACTIVATION = 30;

  public static final String USER_DISABLED_MESSAGE = "User is disabled";
  public static final String USER_LOCKED_MESSAGE = "User is locked";
  public static final String USER_EXPIRED_MESSAGE = "User is expired";
}
