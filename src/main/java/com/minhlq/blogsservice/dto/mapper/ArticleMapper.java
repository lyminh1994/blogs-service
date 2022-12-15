package com.minhlq.blogsservice.dto.mapper;

import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.model.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    /**
     * The mapper instance.
     */
    ArticleMapper MAPPER = Mappers.getMapper(ArticleMapper.class);

    ArticleResponse toArticleResponse(ArticleEntity article, List<String> tagNames);

    ArticleResponse toArticleResponse(ArticleEntity article);
}
