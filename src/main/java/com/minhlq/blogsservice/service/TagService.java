package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.model.Tag;
import org.springframework.data.domain.PageRequest;

public interface TagService {

  PagingResponse<Tag> getTags(PageRequest pageRequest);
}
