package com.minhlq.blogsservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    FEMALE(0, "Female"),
    MALE(1, "Male"),
    OTHER(2, "Other");

    private final Integer code;

    private final String description;
}
