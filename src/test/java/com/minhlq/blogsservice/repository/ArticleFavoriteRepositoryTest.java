package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogsservice.model.ArticleFavoriteEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql({
  "classpath:/sql/users.sql",
  "classpath:/sql/articles.sql",
  "classpath:/sql/articles_favorites.sql"
})
class ArticleFavoriteRepositoryTest {

  @Autowired ArticleFavoriteRepository repository;

  @Test
  void givenThreeArticleFavorites_whenCountArticleFavoritesByArticleIdQuery_thenReturnThree() {
    Long articleIdReturn9 = 1L;
    Long articleIdReturn5 = 10L;
    Long articleIdReturn0 = 11L;
    assertEquals(9, repository.countArticleFavoritesByArticleIdQuery(articleIdReturn9));
    assertEquals(5, repository.countArticleFavoritesByArticleIdQuery(articleIdReturn5));
    assertEquals(0, repository.countArticleFavoritesByArticleIdQuery(articleIdReturn0));
  }

  @Test
  void givenArticleFavorites_whenFindByArticleIdQuery_thenReturnArticleFavorites() {
    List<ArticleFavoriteEntity> articleFavoritesFound = repository.findByArticleIdQuery(1L);
    List<ArticleFavoriteEntity> articleFavoritesNotFound = repository.findByArticleIdQuery(11L);

    assertNotNull(articleFavoritesNotFound);
    assertTrue(articleFavoritesNotFound.isEmpty());
    assertNotNull(articleFavoritesFound);
    assertFalse(articleFavoritesFound.isEmpty());
    assertEquals(9, articleFavoritesFound.size());
    assertEquals(1L, articleFavoritesFound.get(0).getId().getArticleId());
  }
}
