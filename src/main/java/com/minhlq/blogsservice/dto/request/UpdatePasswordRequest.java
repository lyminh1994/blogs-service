package com.minhlq.blogsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {

    private String oldPassword;

    private String newPassword;
}
