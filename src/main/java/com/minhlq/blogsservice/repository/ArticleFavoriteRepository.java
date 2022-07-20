package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.ArticleFavorite;
import com.minhlq.blogsservice.entity.unionkey.ArticleFavoriteKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Article favorite.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface ArticleFavoriteRepository
    extends JpaRepository<ArticleFavorite, ArticleFavoriteKey> {

  @Query(
      "select count(AF.id.articleId) "
          + "from ArticleFavorite AF "
          + "where AF.id.articleId = :articleId")
  long countArticleFavoritesByArticleId(Long articleId);

  @Query("from ArticleFavorite AF where AF.id.articleId = :articleId")
  List<ArticleFavorite> findByArticleId(Long articleId);
}
