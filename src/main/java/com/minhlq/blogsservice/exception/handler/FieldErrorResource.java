package com.minhlq.blogsservice.exception.handler;

public record FieldErrorResource(String resource, String field, String code, String message) {}
