package com.minhlq.blogsservice.constant;

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

  public static final String USER_MUST_NOT_BE_NULL = "User must not be null";
  public static final String USER_DTO_MUST_NOT_BE_NULL = "UserDto must not be null";
  public static final String USER_DETAILS_MUST_NOT_BE_NULL = "User details must not be null";
  public static final String USER_DETAILS_DEBUG_MESSAGE = "User details {}";
  public static final String USER_DISABLED_MESSAGE = "User is disabled";
  public static final String USER_LOCKED_MESSAGE = "User is locked";
  public static final String USER_EXPIRED_MESSAGE = "User is expired";
}
