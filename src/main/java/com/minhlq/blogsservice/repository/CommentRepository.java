package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Article;
import com.minhlq.blogsservice.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Article comments.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findByArticle(Article article, Pageable pageable);

  Optional<Comment> findByIdAndArticle(Long id, Article article);
}
