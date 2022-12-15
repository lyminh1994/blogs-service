package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.model.TagEntity;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
    public PageResponse<TagResponse> getTags(PageRequest pageRequest) {
        Page<TagEntity> tags = tagRepository.findAll(pageRequest);

        return new PageResponse<>(
                tags.stream()
                        .map(entity -> TagResponse.builder().id(entity.getId()).name(entity.getName()).build())
                        .collect(Collectors.toList()),
                tags.getTotalElements());
    }
}
