package com.minhlq.blogs.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonRootName("errors")
@JsonSerialize(using = ErrorResourceSerializer.class)
public record ErrorsResource(List<FieldErrorResource> fieldErrors) {}
