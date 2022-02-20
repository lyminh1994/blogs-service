package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.ArticleFavoriteEntity;
import com.minhlq.blogsservice.entity.unionkey.ArticleFavoriteKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleFavoriteRepository
    extends JpaRepository<ArticleFavoriteEntity, ArticleFavoriteKey>,
        QuerydslPredicateExecutor<ArticleFavoriteEntity> {

  @Query(
      "select count(af.id.articleId) "
          + "from ArticleFavoriteEntity af "
          + "where af.id.articleId = :articleId")
  long countArticleFavoritesByArticleId(Long articleId);

  @Query("from ArticleFavoriteEntity AF where AF.id.articleId = :articleId")
  List<ArticleFavoriteEntity> findByArticleId(Long articleId);
}
