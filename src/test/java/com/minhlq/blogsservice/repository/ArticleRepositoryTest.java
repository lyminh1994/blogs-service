package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.minhlq.blogsservice.model.ArticleEntity;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
/*@Sql({"classpath:/sql/users.sql", "classpath:/sql/articles.sql"})*/
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

  @Autowired ArticleRepository repository;

  @Test
  void givenSetFollowedUsers_whenFindByFollowedUsersQuery_thenReturnCorrect() {
    // given
    Set<Long> followedUsers = Set.of(6L, 10L);

    // when
    Page<ArticleEntity> actual = repository.findByFollowedUsersQuery(followedUsers, null);

    // then
    assertNotNull(actual);
    assertNotNull(actual.getContent());
    assertEquals(5, actual.getTotalElements());
    assertEquals(5, actual.getContent().size());
  }

  @Test
  void givenInvalidFollowedUsers_whenFindByFollowedUsersQuery_thenReturnEmpty() {
    // given
    Set<Long> followedUsers = Set.of(11L);

    // when
    Page<ArticleEntity> actual =
        repository.findByFollowedUsersQuery(followedUsers, Pageable.unpaged());

    // then
    assertNotNull(actual);
    assertEquals(0, actual.getTotalElements());
    assertEquals(0, actual.getContent().size());
  }

  @Test
  void givenNull_whenFindByFollowedUsersQuery_thenReturnEmpty() {
    // when
    Page<ArticleEntity> actual = repository.findByFollowedUsersQuery(null, null);

    // then
    assertNotNull(actual);
    assertEquals(0, actual.getTotalElements());
    assertEquals(0, actual.getContent().size());
  }
}
