package com.minhlq.blogsservice.dto.request;

import com.minhlq.blogsservice.enums.Gender;
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
