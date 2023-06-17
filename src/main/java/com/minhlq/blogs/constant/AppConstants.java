package com.minhlq.blogs.constant;

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

  public static final String DEFAULT_CURRENT_AUDITOR = "system";
  public static final String ALL_PATTERN_ENDPOINT = "/**";

  public static final String ARTICLES_ENDPOINT = "/articles";
  public static final String FEEDS_ENDPOINT = "/feeds";
  public static final String SLUG_ENDPOINT = "/{slug}";
  public static final String FAVORITE_ENDPOINT = "/{slug}/favorite";

  public static final String AUTHENTICATION_ENDPOINT = "/auth";
  public static final String REGISTER_ENDPOINT = "/register";
  public static final String LOGIN_ENDPOINT = "/login";
  public static final String REFRESH_TOKEN_ENDPOINT = "/refresh-token";
  public static final String LOGOUT_ENDPOINT = "/logout";
  public static final String VERIFY_ENDPOINT = "/verify/{verifyToken}";

  public static final String COMMENTS_ENDPOINT = "{slug}/comments";
  public static final String COMMENT_ENDPOINT = "/{commentId}";

  public static final String TAGS_ENDPOINT = "/tags";

  public static final String CURRENT_USER_ENDPOINT = "/user";
  public static final String CHANGE_PASSWORD_ENDPOINT = "/password";
  public static final String PROFILE_ENDPOINT = "/{username}";
  public static final String FOLLOWING_ENDPOINT = "/{username}/following";
}
