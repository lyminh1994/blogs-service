package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.entity.ArticleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleMapper {

  ArticleMapper MAPPER = Mappers.getMapper(ArticleMapper.class);

  ArticleResponse toArticleResponse(ArticleEntity article, List<String> tagNames);

  ArticleResponse toArticleResponse(ArticleEntity article);
}
