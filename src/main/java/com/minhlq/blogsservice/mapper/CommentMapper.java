package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

  CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

  CommentResponse toCommentResponse(CommentEntity comment);
}
