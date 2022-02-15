package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.ArticleFavoriteEntity;
import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleFavoriteRepository
    extends JpaRepository<ArticleFavoriteEntity, ArticleFavoriteKey>,
        QuerydslPredicateExecutor<ArticleFavoriteEntity> {

  @Query(
      "select count(af.id.articleId) from ArticleFavoriteEntity af where af.id.articleId = :articleId")
  long countArticleFavoritesByArticleId(Long articleId);

  List<ArticleFavoriteEntity> findByArticle(ArticleEntity article);
}
