package com.minhlq.blogsservice.dto;

import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.validation.UpdateUserConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@UpdateUserConstraint
public class UpdateUserDto {

  private final UserPrincipal targetUser;

  private final UpdateUserRequest params;
}
