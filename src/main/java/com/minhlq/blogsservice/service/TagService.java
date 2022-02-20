package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.response.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface TagService {

  PageResponse<String> getTags(PageRequest pageRequest);
}
