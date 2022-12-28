package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagsController.class)
class TagsControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean TagService tagService;
  @MockBean JwtService jwtService;
  @MockBean UserDetailsService userDetailsService;
  @MockBean EncryptionService encryptionService;

  @Test
  @DisplayName("Should Get Tag Success GET request to endpoint - /tags")
  void shouldGetTagSuccess() throws Exception {
    // given - precondition or setup
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.unsorted());
    TagResponse content1 = TagResponse.builder().id(1L).name("tag1").build();
    TagResponse content2 = TagResponse.builder().id(2L).name("tag2").build();
    List<TagResponse> expectedContents = Arrays.asList(content1, content2);
    PageResponse<TagResponse> pageResponse =
        new PageResponse<>(expectedContents, expectedContents.size());
    given(tagService.getTags(pageRequest)).willReturn(pageResponse);

    // when - action or behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/tags?page-number=0&page-size=10"));

    // then - verify the result or output using assert statements
    response
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.contents", is(expectedContents)))
        .andExpect(jsonPath("$.contents[0]", is(expectedContents.get(0))))
        .andExpect(jsonPath("$.contents[1]", is(expectedContents.get(1))))
        .andExpect(jsonPath("$.total", is(expectedContents.size())));
  }
}
