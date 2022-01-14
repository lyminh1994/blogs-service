package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleFavorite;
import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleFavoriteRepository
    extends JpaRepository<ArticleFavorite, ArticleFavoriteId>,
        QuerydslPredicateExecutor<ArticleFavorite> {}
