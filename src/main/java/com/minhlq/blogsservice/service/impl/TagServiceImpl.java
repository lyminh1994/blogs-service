package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.response.PagingResponse;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public PagingResponse<String> getTags(PageRequest pageRequest) {
    Page<String> tagNames = tagRepository.findNames(pageRequest);
    return new PagingResponse<>(tagNames.getContent(), tagNames.getTotalElements());
  }
}
