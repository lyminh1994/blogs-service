package com.minhlq.blogsservice.payload.request;

import com.minhlq.blogsservice.constant.UserConstants;
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

  @NotBlank(message = UserConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
  private String username;

  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, message = UserConstants.PASSWORD_SIZE)
  private String password;
}
