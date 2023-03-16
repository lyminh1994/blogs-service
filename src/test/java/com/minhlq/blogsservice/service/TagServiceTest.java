package com.minhlq.blogsservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.model.TagEntity;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.impl.TagServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

  @Mock TagRepository tagRepository;

  @InjectMocks TagServiceImpl tagService;

  @Test
  @DisplayName("Should get tags success")
  void shouldGetTagsSuccess() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    List<TagEntity> contents =
        Arrays.asList(
            new TagEntity(1L, "tag1"), new TagEntity(2L, "tag2"), new TagEntity(3L, "tag3"));
    long total = 3;
    Page<TagEntity> tags = new PageImpl<>(contents, pageRequest, total);
    when(tagRepository.findAll(pageRequest)).thenReturn(tags);
    PageResponse<TagResponse> actualResponse = tagService.getTags(pageRequest);

    assertNotNull(actualResponse.contents());
    assertEquals(total, actualResponse.totalElements());
  }
}
