package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Article;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Article.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

  Optional<Article> findBySlug(String slug);

  @Query(value = "from Article A where A.author.id in (:followedUsers)")
  Page<Article> findByFollowedUsers(Set<Long> followedUsers, Pageable pageable);
}
