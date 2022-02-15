package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleRepository
    extends JpaRepository<ArticleEntity, String>, QuerydslPredicateExecutor<ArticleEntity> {

  Optional<ArticleEntity> findBySlug(String slug);

  @Query(value = "from ArticleEntity A where A.author.id in (:followedUsers)")
  Page<ArticleEntity> findByFollowedUsers(Set<Long> followedUsers, Pageable pageable);
}