package com.minhlq.blogs.payload;

import com.minhlq.blogs.enums.Gender;
import java.time.LocalDate;

/**
 * This class models the format of the user response produced.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public record UserResponse(
    String publicId,
    String email,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthday,
    Gender gender,
    String profileImage) {
}
