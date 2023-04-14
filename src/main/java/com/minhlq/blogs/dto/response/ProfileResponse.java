package com.minhlq.blogs.dto.response;

import com.minhlq.blogs.enums.Gender;
import java.time.LocalDate;
import lombok.Data;

/**
 * This class models the format of the user profile response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
public class ProfileResponse {

  private String publicId;

  private String name;

  private String phone;

  private LocalDate birthday;

  private Gender gender;

  private String profileImage;

  private boolean following;
}
