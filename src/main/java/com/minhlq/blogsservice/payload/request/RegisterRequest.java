package com.minhlq.blogsservice.payload.request;

import com.minhlq.blogsservice.annotation.validation.DuplicatedUsernameConstraint;
import com.minhlq.blogsservice.constant.UserConstants;
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
  @NotBlank(message = UserConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
  @DuplicatedUsernameConstraint
  private String username;

  @ToString.Exclude
  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, message = UserConstants.PASSWORD_SIZE)
  private String password;
}
