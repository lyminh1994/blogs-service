package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.ArticleEntity;
import com.minhlq.blogsservice.entity.CommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Article comments.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CommentRepository
    extends JpaRepository<CommentEntity, String>, QuerydslPredicateExecutor<CommentEntity> {

  List<CommentEntity> findByArticle(ArticleEntity article);

  Optional<CommentEntity> findByIdAndArticle(Long id, ArticleEntity article);
}
