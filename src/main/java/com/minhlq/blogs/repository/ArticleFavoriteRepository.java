package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.ArticleFavoriteEntity;
import com.minhlq.blogs.model.unionkey.ArticleFavoriteKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Article favorite.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ArticleFavoriteRepository
    extends JpaRepository<ArticleFavoriteEntity, ArticleFavoriteKey> {

  @Query(
      "select count(AF.id.articleId) "
          + "from ArticleFavoriteEntity AF "
          + "where AF.id.articleId = :articleId")
  long countArticleFavoritesByArticleIdQuery(Long articleId);

  @Query("from ArticleFavoriteEntity AF where AF.id.articleId = :articleId")
  List<ArticleFavoriteEntity> findByArticleIdQuery(Long articleId);
}
