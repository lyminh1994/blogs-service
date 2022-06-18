package com.minhlq.blogsservice.payload.response;

import lombok.Builder;
import lombok.Data;

/**
 * This class models the format of the user response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public final class UserResponse {

  private Long id;

  private String username;

  private String email;

  private String bio;

  private String image;
}
