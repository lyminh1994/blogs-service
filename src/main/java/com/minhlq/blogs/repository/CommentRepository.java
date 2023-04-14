package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.ArticleEntity;
import com.minhlq.blogs.model.CommentEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Article comments.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  Page<CommentEntity> findByArticle(ArticleEntity article, Pageable pageable);

  Optional<CommentEntity> findByIdAndArticle(Long id, ArticleEntity article);
}
