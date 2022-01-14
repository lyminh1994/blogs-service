package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.PagingResponse;
import org.springframework.data.domain.PageRequest;

public interface TagService {

  PagingResponse<String> getTags(PageRequest pageRequest);
}
