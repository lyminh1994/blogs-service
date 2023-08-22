package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.ArticleEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Article.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

  Optional<ArticleEntity> findBySlug(String slug);

  @Query("from ArticleEntity A where A.author.id in (:followedUsers)")
  Page<ArticleEntity> findByFollowedUsersQuery(Set<Long> followedUsers, Pageable pageable);

  boolean existsBySlug(String slug);
}
