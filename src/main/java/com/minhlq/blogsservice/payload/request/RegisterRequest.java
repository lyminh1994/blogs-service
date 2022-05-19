package com.minhlq.blogsservice.payload.request;

import com.minhlq.blogsservice.validation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.validation.DuplicatedUsernameConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * This class models the format of the register request allowed through the controller endpoints.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public final class RegisterRequest {

  @NotBlank(message = "can't be empty")
  @Email(message = "should be an email")
  @DuplicatedEmailConstraint
  private String email;

  @NotBlank(message = "can't be empty")
  @DuplicatedUsernameConstraint
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;
}
