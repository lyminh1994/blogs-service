package com.minhlq.blogsservice.dto.response;

import java.util.List;
import lombok.Value;

@Value
public class PageResponse<T> {

  List<T> contents;

  long totalElements;
}
