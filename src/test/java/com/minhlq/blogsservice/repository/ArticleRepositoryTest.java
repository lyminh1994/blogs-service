package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogsservice.entity.ArticleEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = {"classpath:db/users.sql", "classpath:db/articles.sql"})
class ArticleRepositoryTest extends BaseRepositoryTest {

  @Autowired ArticleRepository articleRepository;

  @Test
  @DisplayName("Should Find By Slug Success")
  void shouldFindBySlugSuccess() {
    Optional<ArticleEntity> actual = articleRepository.findBySlug("article-3");

    assertTrue(actual.isPresent());
  }

  @Test
  @DisplayName("Should Find By Followed Users Success")
  void shouldFindByFollowedUsersSuccess() {
    Set<Long> followedUsers = Collections.singleton(2L);
    Pageable pageable = PageRequest.of(0, 10);
    Page<ArticleEntity> actual = articleRepository.findByFollowedUsers(followedUsers, pageable);

    assertEquals(2L, actual.getTotalElements());
    assertNotNull(actual.getContent());
    assertEquals(2, actual.getContent().size());
  }
}
