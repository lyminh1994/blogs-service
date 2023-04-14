package com.minhlq.blogs.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  ROLE_ANONYMOUS(0, "Anonymous"),
  ROLE_USER(1, "Normal user"),
  ROLE_ADMIN(2, "Super user");

  private final Integer code;

  private final String description;

  public boolean match(UserRole role) {
    return Arrays.stream(UserRole.values())
        .anyMatch(userRole -> userRole.name().equals(role.name()));
  }
}
