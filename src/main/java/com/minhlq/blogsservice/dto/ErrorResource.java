package com.minhlq.blogsservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("errors")
public record ErrorResource(Object resource, Object field, Integer code, String message) {}
