package com.minhlq.blogsservice.controller;

import static com.minhlq.blogsservice.constant.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.minhlq.blogsservice.constant.AppConstants.DEFAULT_PAGE_SIZE;

import com.minhlq.blogsservice.dto.response.PageResponse;
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

@Tag(name = "Tags", description = "Tags APIs")
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagsController {

  private final TagService tagService;

  @SecurityRequirements
  @Operation(summary = "Get tags", description = "Get all tags")
  @GetMapping
  public PageResponse<String> getTags(
      @RequestParam(name = "page-number", required = false, defaultValue = DEFAULT_PAGE_NUMBER)
          int pageNumber,
      @RequestParam(name = "page-size", required = false, defaultValue = DEFAULT_PAGE_SIZE)
          int pageSize,
      @RequestParam(name = "sort", required = false) String[] sort) {

    PageRequest pageRequest = PagingUtils.toPageRequest(pageNumber, pageSize, sort);
    return tagService.getTags(pageRequest);
  }
}
