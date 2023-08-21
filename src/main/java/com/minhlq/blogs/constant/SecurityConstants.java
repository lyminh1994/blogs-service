package com.minhlq.blogs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all security-related URL mappings constants.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

  public static final int DEFAULT_TOKEN_DURATION = 7;
  public static final int SECURITY_STRENGTH = 12;
}
