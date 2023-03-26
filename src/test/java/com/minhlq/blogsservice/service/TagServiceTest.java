package com.minhlq.blogsservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.model.TagEntity;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.impl.TagServiceImpl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

  @Mock TagRepository tagRepository;

  @InjectMocks TagServiceImpl tagService;

  @Test
  @DisplayName("Call get tags will return correct")
  void given_whenGetTags_thenCorrect() {
    // given
    TagEntity tag1 = new TagEntity(1L, "tag1");
    TagEntity tag2 = new TagEntity(2L, "tag2");
    TagEntity tag3 = new TagEntity(3L, "tag3");
    Page<TagEntity> tags = new PageImpl<>(List.of(tag1, tag2, tag3));

    given(tagRepository.findAll(Pageable.unpaged())).willReturn(tags);

    // when
    PageResponse<TagResponse> actual = tagService.getTags(Pageable.unpaged());

    // then
    assertNotNull(actual.contents());
    assertFalse(actual.contents().isEmpty());
    assertEquals(3, actual.totalElements());
  }

  @Test
  void givenEmptyPage_whenGetTags_thenReturnEmpty() {
    // given
    given(tagRepository.findAll(Pageable.unpaged())).willReturn(Page.empty());

    // when
    PageResponse<TagResponse> actual = tagService.getTags(Pageable.unpaged());

    // then
    assertNotNull(actual);
    assertNotNull(actual.contents());
    assertTrue(actual.contents().isEmpty());
    assertEquals(0, actual.totalElements());
  }
}
