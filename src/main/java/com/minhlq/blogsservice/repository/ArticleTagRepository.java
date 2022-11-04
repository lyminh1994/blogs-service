package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleTagEntity;
import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Article tags.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface ArticleTagRepository extends JpaRepository<ArticleTagEntity, ArticleTagKey> {

  @Query("from ArticleTagEntity AT where AT.id.articleId = :articleId")
  List<ArticleTagEntity> findByArticleId(Long articleId);
}
