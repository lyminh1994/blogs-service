package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.ArticleResponse;
import com.minhlq.blogsservice.model.Article;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleMapper {

  ArticleMapper MAPPER = Mappers.getMapper(ArticleMapper.class);

  ArticleResponse toArticleResponse(Article article, List<String> tagNames);

  ArticleResponse toArticleResponse(Article article);
}
