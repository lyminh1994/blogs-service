package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.model.Tag;
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
  public PagingResponse<Tag> getTags(PageRequest pageRequest) {
    Page<Tag> tags = tagRepository.findAll(pageRequest);
    return new PagingResponse<>(tags.getContent(), tags.getTotalElements());
  }
}
