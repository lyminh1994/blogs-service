package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.dto.response.TagResponse;
import com.minhlq.blogs.repository.TagRepository;
import com.minhlq.blogs.service.TagService;
import lombok.RequiredArgsConstructor;
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
    var tags = tagRepository.findAll(pageable);

    return new PageResponse<>(
        tags.stream().map(entity -> new TagResponse(entity.getId(), entity.getName())).toList(),
        tags.getTotalElements());
  }
}
