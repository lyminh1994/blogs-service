package com.minhlq.blogsservice.payload.request;

import static com.minhlq.blogsservice.constant.UserConstants.INVALID_PASSWORD_SIZE;
import static com.minhlq.blogsservice.constant.UserConstants.INVALID_USERNAME_SIZE;
import static com.minhlq.blogsservice.constant.UserConstants.PASSWORD_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_CANNOT_BLANK;
import static com.minhlq.blogsservice.constant.UserConstants.USERNAME_EXISTED;

import com.minhlq.blogsservice.annotation.DuplicatedUsernameConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class models the format of the register request allowed through the controller endpoints.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class RegisterRequest {

  @EqualsAndHashCode.Include
  @NotBlank(message = USERNAME_CANNOT_BLANK)
  @Size(min = 3, max = 50, message = INVALID_USERNAME_SIZE)
  @DuplicatedUsernameConstraint(message = USERNAME_EXISTED)
  private String username;

  @ToString.Exclude
  @NotBlank(message = PASSWORD_CANNOT_BLANK)
  @Size(min = 4, message = INVALID_PASSWORD_SIZE)
  private String password;
}
