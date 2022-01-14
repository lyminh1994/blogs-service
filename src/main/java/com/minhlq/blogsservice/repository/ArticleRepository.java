package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Article;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleRepository
    extends JpaRepository<Article, String>, QuerydslPredicateExecutor<Article> {

  Optional<Article> findBySlug(String slug);

  @Query(value = "from Article A where A.author.id in (:followedUsers)")
  Page<Article> findByFollowedUsers(Set<Long> followedUsers, Pageable pageable);

}
