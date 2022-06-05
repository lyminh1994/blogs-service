package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all profile type constants used in the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileTypeConstants {

  /** The dev profile exposes development specific beans and configurations. */
  public static final String DEV = "dev";

  /** The prod profile exposes production specific beans and configurations. */
  public static final String PROD = "prod";

  /** The test profile exposes testing specific beans and configurations. */
  public static final String TEST = "test";
}
