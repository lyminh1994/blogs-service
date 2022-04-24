package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minhlq.blogsservice.entity.ArticleFavoriteEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:db/article_favorites.sql")
class ArticleFavoriteRepositoryTest extends BaseRepositoryTest {

  @Autowired ArticleFavoriteRepository articleFavoriteRepository;

  @Test
  @DisplayName("Should Count Article Favorites By Article Id Success")
  void shouldCountArticleFavoritesByArticleIdSuccess() {
    long actual = articleFavoriteRepository.countArticleFavoritesByArticleId(1L);

    assertEquals(4, actual);
  }

  @Test
  @DisplayName("Should Find By Article Id Success")
  void shouldFindByArticleIdSuccess() {
    Long articleId = 1L;
    List<ArticleFavoriteEntity> actual = articleFavoriteRepository.findByArticleId(articleId);
    List<Long> articles =
        actual.stream()
            .map(articleFavorite -> articleFavorite.getId().getArticleId())
            .distinct()
            .collect(Collectors.toList());

    List<Long> users =
        actual.stream()
            .map(articleFavorite -> articleFavorite.getId().getUserId())
            .sorted()
            .collect(Collectors.toList());

    assertNotNull(actual);
    assertEquals(Collections.singletonList(1L), articles);
    assertEquals(Arrays.asList(1L, 2L, 3L, 4L), users);
  }
}
