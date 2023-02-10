package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.dto.response.TagResponse;
import org.springframework.data.domain.PageRequest;

/**
 * The tag service to provide for the tag operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface TagService {

  /**
   * Get all tags name with paging params.
   *
   * @param pageRequest the paging params
   * @return paging tags
   */
  PageResponse<TagResponse> getTags(PageRequest pageRequest);
}
