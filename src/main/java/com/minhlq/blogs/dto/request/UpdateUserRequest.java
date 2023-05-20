package com.minhlq.blogs.dto.request;

import com.minhlq.blogs.enums.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public record UpdateUserRequest(
    @Email String email,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthday,
    Gender gender,
    String profileImage) {}
