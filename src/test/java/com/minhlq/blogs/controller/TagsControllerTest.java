package com.minhlq.blogs.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.dto.response.TagResponse;
import com.minhlq.blogs.service.TagService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Disabled("not ready yet")
@WebMvcTest(TagsController.class)
class TagsControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean TagService tagService;

  @Test
  @DisplayName("Should Get Tag Success GET request to endpoint - /tags")
  void shouldGetTagSuccess() throws Exception {
    // given - precondition or setup
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.unsorted());
    TagResponse content1 = new TagResponse(1L, "tag1");
    TagResponse content2 = new TagResponse(2L, "tag2");
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
