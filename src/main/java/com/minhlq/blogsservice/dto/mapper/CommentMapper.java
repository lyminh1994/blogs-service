package com.minhlq.blogsservice.dto.mapper;

import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The CommentMapper class outlines the supported conversions between Comment entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper
public interface CommentMapper {

  /** The mapper instance. */
  CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

  CommentResponse toCommentResponse(Comment comment);
}