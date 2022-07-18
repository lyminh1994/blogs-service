package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Article;
import com.minhlq.blogsservice.entity.Comment;
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
public interface CommentRepository extends JpaRepository<Comment, String> {

  Page<Comment> findByArticle(Article article, Pageable pageable);

  Optional<Comment> findByIdAndArticle(Long id, Article article);
}
