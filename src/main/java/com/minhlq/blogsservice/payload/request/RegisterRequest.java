package com.minhlq.blogsservice.payload.request;

import com.minhlq.blogsservice.annotation.validation.DuplicatedEmailConstraint;
import com.minhlq.blogsservice.annotation.validation.DuplicatedUsernameConstraint;
import javax.validation.constraints.Email;
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
  @NotBlank(message = "Username cannot be blank")
  @Size(min = 3, max = 50, message = "Username should be at least 3 and at most 50 characters")
  @DuplicatedUsernameConstraint
  private String username;

  @Size(max = 60)
  @EqualsAndHashCode.Include
  @NotBlank(message = "Email cannot be blank")
  @Email(message = "A valid email format is required")
  @DuplicatedEmailConstraint
  private String email;

  @ToString.Exclude
  @NotBlank(message = "Password cannot be blank")
  @Size(min = 4, max = 15, message = "Password should be at least 4 characters")
  private String password;
}
