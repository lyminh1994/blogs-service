package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.constant.AppConstants;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import com.minhlq.blogsservice.service.TagService;
import com.minhlq.blogsservice.util.PagingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
   * @param pageNumber the page number
   * @param pageSize the page size
   * @param sort the sort fields
   * @return paged tags name
   */
  @GetMapping
  @SecurityRequirements
  @Operation(summary = "Get tags", description = "Get all tags name")
  public PageResponse<TagResponse> getTags(
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
      @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) int pageSize,
      @RequestParam(required = false) String[] sort) {

    PageRequest pageRequest = PagingUtils.toPageRequest(pageNumber, pageSize, sort);
    return tagService.getTags(pageRequest);
  }
}
