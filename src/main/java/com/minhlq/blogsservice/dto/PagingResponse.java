package com.minhlq.blogsservice.dto;

import java.util.List;
import lombok.Value;

@Value
public class PagingResponse<T> {

  List<T> contents;

  long total;
}
