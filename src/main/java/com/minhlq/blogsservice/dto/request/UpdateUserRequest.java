package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.enums.Gender;
import java.time.Instant;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the update user request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

  @Email(message = "should be an email")
  private String email;

  private String firstName;

  private String lastName;

  private String phone;

  private Instant birthday;

  private Gender gender;

  private String bio;

  private String image;
}
