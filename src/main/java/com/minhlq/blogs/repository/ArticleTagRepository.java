package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.ArticleTagEntity;
import com.minhlq.blogs.model.unionkey.ArticleTagKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Article tags.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTagEntity, ArticleTagKey> {

  @Query("from ArticleTagEntity AT where AT.id.articleId = :articleId")
  List<ArticleTagEntity> findByArticleIdQuery(@Param("articleId") Long articleId);
}
