package com.minhlq.blogs.mapper;

import com.minhlq.blogs.dto.response.ArticleResponse;
import com.minhlq.blogs.model.ArticleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * The ArticleMapper class outlines the supported conversions between Article entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper
public interface ArticleMapper {

  /** The mapper instance. */
  ArticleMapper MAPPER = Mappers.getMapper(ArticleMapper.class);

  @Mappings(
      value = {
        @Mapping(target = "favorite", ignore = true),
        @Mapping(target = "favoritesCount", ignore = true)
      })
  ArticleResponse toArticleResponse(ArticleEntity article, List<String> tagNames);

  @Mappings(
      value = {
        @Mapping(target = "favorite", ignore = true),
        @Mapping(target = "favoritesCount", ignore = true),
        @Mapping(target = "tagNames", ignore = true)
      })
  ArticleResponse toArticleResponse(ArticleEntity article);
}
