package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.CommentResponse;
import com.minhlq.blogsservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

  CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

  CommentResponse toCommentResponse(Comment comment);
}
