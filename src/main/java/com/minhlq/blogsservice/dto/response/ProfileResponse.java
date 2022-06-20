package com.minhlq.blogsservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the user profile response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

  private String username;

  private String bio;

  private String image;

  private boolean following;
}
