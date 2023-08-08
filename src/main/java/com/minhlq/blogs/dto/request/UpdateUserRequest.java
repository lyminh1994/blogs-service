package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.enums.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

/**
 * This record is used to represent a request to update user information
 *
 * @author  MinhLy
 * @version  1.0
 * @since  2023-08-09
 */
public record UpdateUserRequest(
    @Email String email,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthday,
    Gender gender,
    String profileImage) {}
