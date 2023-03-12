package com.minhlq.blogsservice.exception.handler;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("errors")
public record ErrorResource(Object resource, Object field, Object code, String message) {}
