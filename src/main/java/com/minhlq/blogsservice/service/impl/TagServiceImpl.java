package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.model.Tag;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.TagService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public List<String> getTags() {
    return tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toList());
  }
}
