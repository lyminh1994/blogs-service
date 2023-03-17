package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all the error messages used in the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorConstants {

  /** Invalid token. */
  public static final String INVALID_TOKEN = "Invalid token";

  /** Unauthorized Access detected. When the user is not authorized to access the resource. */
  public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access detected...";

  public static final String VERIFY_TOKEN_EXPIRED = "Verification token was expire";
}
