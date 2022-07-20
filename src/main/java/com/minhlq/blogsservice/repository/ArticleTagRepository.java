package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.ArticleTag;
import com.minhlq.blogsservice.entity.unionkey.ArticleTagKey;
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
public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagKey> {

  @Query("from ArticleTag AT where AT.id.articleId = :articleId")
  List<ArticleTag> findByArticleId(Long articleId);
}
