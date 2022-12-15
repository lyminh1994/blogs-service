package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the Article comments.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByArticle(ArticleEntity article, Pageable pageable);

    Optional<CommentEntity> findByIdAndArticle(Long id, ArticleEntity article);
}
