package com.minhlq.blogsservice.payload.request;

import javax.validation.constraints.NotBlank;
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

  @NotBlank(message = "can't be empty")
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;
}
