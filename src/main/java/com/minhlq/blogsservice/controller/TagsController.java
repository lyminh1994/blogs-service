package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@Tag(name = "Tags", description = "Tags APIs")
@RequiredArgsConstructor
public class TagsController {

  private final TagService tagService;

  @Operation(summary = "Get tags", description = "Get all tags")
  @GetMapping
  public List<String> getTags() {
    return tagService.getTags();
  }
}
