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

  public static final String ALL_PATTERN = "/**";

  public static final String ARTICLES = "/articles";
  public static final String FEEDS = "/feeds";
  public static final String SLUG = "/{slug}";
  public static final String FAVORITE = "/{slug}/favorite";

  public static final String SIGN_UP = "/sign-up";
  public static final String SIGN_IN = "/sign-in";
  public static final String REFRESH_TOKEN = "/refresh-token";
  public static final String SIGN_OUT = "/sign-out";
  public static final String VERIFY = "/verify/{verifyToken}";

  public static final String COMMENTS = "{slug}/comments";
  public static final String COMMENT = "/{commentId}";

  public static final String TAGS = "/tags";

  public static final String USER = "/user";
  public static final String PASSWORD = "/password";
  public static final String USERNAME = "/{username}";
  public static final String FOLLOWING = "/{username}/following";
}
