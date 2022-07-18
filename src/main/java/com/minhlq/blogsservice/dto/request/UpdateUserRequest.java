package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.enums.Gender;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import lombok.Getter;

/**
 * This class models the format of the update user request accepted.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
public class UpdateUserRequest {

  @Email(message = "Should be an email")
  private String email;

  private String firstName;

  private String lastName;

  private String phone;

  private LocalDate birthday;

  private Gender gender;

  private String profileImage;
}
