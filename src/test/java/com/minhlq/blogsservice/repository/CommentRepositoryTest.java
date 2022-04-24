package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogsservice.entity.ArticleEntity;
import com.minhlq.blogsservice.entity.CommentEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = {"classpath:db/users.sql", "classpath:db/articles.sql", "classpath:db/comments.sql"})
class CommentRepositoryTest extends BaseRepositoryTest {

  @Autowired CommentRepository commentRepository;

  @Test
  @DisplayName("Should Find By Article Success")
  void shouldFindByArticleSuccess() {
    ArticleEntity article = new ArticleEntity();
    article.setId(1L);
    List<CommentEntity> actual = commentRepository.findByArticle(article);

    assertNotNull(actual);
    assertEquals(2, actual.size());
  }

  @Test
  @DisplayName("Should Find By Id And Article Success")
  void shouldFindByIdAndArticleSuccess() {
    ArticleEntity article = new ArticleEntity();
    article.setId(1L);
    Optional<CommentEntity> actual = commentRepository.findByIdAndArticle(1L, article);

    assertTrue(actual.isPresent());
    assertEquals("body 1", actual.get().getBody());
  }
}
