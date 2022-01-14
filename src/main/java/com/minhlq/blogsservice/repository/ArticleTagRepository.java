package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleTag;
import com.minhlq.blogsservice.model.unionkey.ArticleTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleTagRepository
    extends JpaRepository<ArticleTag, ArticleTagId>, QuerydslPredicateExecutor<ArticleTag> {}
