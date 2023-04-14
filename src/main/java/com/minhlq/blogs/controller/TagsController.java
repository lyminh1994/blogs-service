package com.minhlq.blogs.controller;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.dto.response.TagResponse;
import com.minhlq.blogs.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles all requests relating to tag.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.TAGS)
@Tag(name = "Tags", description = "Blog Article Tags APIs")
public class TagsController {

  private final TagService tagService;

  /**
   * Get all tags name.
   *
   * @param pageable paging
   * @return paged tags name
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get tags", description = "Get all tags")
  public PageResponse<TagResponse> getTags(@ParameterObject Pageable pageable) {
    return tagService.getTags(pageable);
  }
}
