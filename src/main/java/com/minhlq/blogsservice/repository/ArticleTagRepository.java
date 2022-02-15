package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.ArticleTagEntity;
import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleTagRepository
    extends JpaRepository<ArticleTagEntity, ArticleTagKey>,
        QuerydslPredicateExecutor<ArticleTagEntity> {

  List<ArticleTagEntity> findByArticle(ArticleEntity article);
}
