package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.minhlq.blogsservice.model.ArticleTagEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql({
  "classpath:/sql/users.sql",
  "classpath:/sql/articles.sql",
  "classpath:/sql/tags.sql",
  "classpath:/sql/articles_tags.sql"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleTagRepositoryTest {

  @Autowired ArticleTagRepository repository;

  @Test
  void givenArticleId_whenFindByArticleIdQuery_thenReturnCorrectList() {
    // given
    Long articleId = 1L;

    // when
    List<ArticleTagEntity> actual = repository.findByArticleIdQuery(articleId);

    // then
    assertNotNull(actual);
    assertEquals(9, actual.size());
    assertEquals(1L, actual.get(0).getId().getArticleId());
  }

  @Test
  void givenIncorrectArticleId_whenCallFindByArticleIdQuery_thenReturnEmpty() {
    // given
    Long articleId = 199L;

    // when
    List<ArticleTagEntity> actual = repository.findByArticleIdQuery(articleId);

    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }

  @Test
  void givenNullArticleId_whenCallFindByArticleIdQuery_thenReturnEmpty() {
    // when
    List<ArticleTagEntity> actual = repository.findByArticleIdQuery(null);

    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }
}
