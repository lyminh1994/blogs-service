package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.service.TagService;
import com.minhlq.blogsservice.util.PagingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@Tag(name = "Tags", description = "Tags APIs")
@RequiredArgsConstructor
public class TagsController {

  private final TagService tagService;

  @Operation(summary = "Get tags", description = "Get all tags")
  @GetMapping
  public PagingResponse<String> getTags(
      @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
      @RequestParam(name = "sort", required = false) String[] sort) {
    return tagService.getTags(PagingUtils.toPageRequest(pageNumber, pageSize, sort));
  }
}
