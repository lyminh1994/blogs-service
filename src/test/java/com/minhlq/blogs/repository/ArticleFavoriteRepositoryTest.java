package com.minhlq.blogs.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogs.model.ArticleFavoriteEntity;
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
  "classpath:/sql/articles_favorites.sql"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleFavoriteRepositoryTest {

  @Autowired ArticleFavoriteRepository repository;

  @Test
  void whenCountArticleFavoritesByArticleIdQuery_thenReturnCorrect() {
    // when
    long actual = repository.countArticleFavoritesByArticleIdQuery(1L);
    long actual1 = repository.countArticleFavoritesByArticleIdQuery(10L);
    long actual2 = repository.countArticleFavoritesByArticleIdQuery(null);

    // then
    assertEquals(9, actual);
    assertEquals(5, actual1);
    assertEquals(0, actual2);
  }

  @Test
  void whenFindByArticleIdQuery_thenReturnCorrect() {
    // when
    List<ArticleFavoriteEntity> articleFavoritesFound = repository.findByArticleIdQuery(1L);
    List<ArticleFavoriteEntity> articleFavoritesNotFound = repository.findByArticleIdQuery(11L);
    List<ArticleFavoriteEntity> articleFavoritesNull = repository.findByArticleIdQuery(null);

    // then
    assertNotNull(articleFavoritesNotFound);
    assertTrue(articleFavoritesNotFound.isEmpty());
    assertNotNull(articleFavoritesFound);
    assertFalse(articleFavoritesFound.isEmpty());
    assertEquals(9, articleFavoritesFound.size());
    assertEquals(1L, articleFavoritesFound.get(0).getId().getArticleId());
    assertNotNull(articleFavoritesNull);
    assertTrue(articleFavoritesNull.isEmpty());
  }
}
