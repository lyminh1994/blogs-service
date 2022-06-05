package com.minhlq.blogsservice.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the login request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LoginRequest {

  @NotBlank(message = "Username cannot be blank")
  @Size(min = 3, max = 50, message = "Username should be at least 3 and at most 50 characters")
  private String username;

  @NotBlank(message = "Password cannot be left blank")
  @Size(min = 4, message = "Password should be at least 4 characters")
  private String password;
}
