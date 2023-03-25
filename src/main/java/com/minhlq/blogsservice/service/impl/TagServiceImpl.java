package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.model.TagEntity;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * This is implement for the tag service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public PageResponse<TagResponse> getTags(Pageable pageable) {
    Page<TagEntity> tags = tagRepository.findAll(pageable);

    return new PageResponse<>(
        tags.stream().map(entity -> new TagResponse(entity.getId(), entity.getName())).toList(),
        tags.getTotalElements());
  }
}
