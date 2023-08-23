package com.minhlq.blogs.mapper;

import com.minhlq.blogs.dto.response.CommentResponse;
import com.minhlq.blogs.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The CommentMapper class outlines the supported conversions between Comment entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(target = "user.following", ignore = true)
  CommentResponse toCommentResponse(CommentEntity comment);
}
