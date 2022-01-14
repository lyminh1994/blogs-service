package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Article;
import com.minhlq.blogsservice.model.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository
    extends JpaRepository<Comment, String>, QuerydslPredicateExecutor<Comment> {

  List<Comment> findByArticle(Article article);

  Optional<Comment> findByIdAndArticle(Long id, Article article);
}
