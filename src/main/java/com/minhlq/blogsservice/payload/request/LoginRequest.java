package com.minhlq.blogsservice.payload.request;

import static com.minhlq.blogsservice.constant.UserConstants.PASSWORD_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_PASSWORD_SIZE;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_USERNAME_SIZE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * This class models the format of the login request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public final class LoginRequest {

  @NotBlank(message = USERNAME_CANNOT_BLANK)
  @Size(min = 3, max = 50, message = INVALID_USERNAME_SIZE)
  private String username;

  @NotBlank(message = PASSWORD_CANNOT_BLANK)
  @Size(min = 4, message = INVALID_PASSWORD_SIZE)
  private String password;
}
