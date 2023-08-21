package com.minhlq.blogs.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.minhlq.blogs.helper.EnableTestcontainers;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@EnableTestcontainers
@Sql({"classpath:/sql/users.sql", "classpath:/sql/follows.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FollowRepositoryTest {

  @Autowired FollowRepository repository;

  @Test
  void givenCorrectUserId_whenCallFindByUserIdQuery_thenReturnCorrect() {
    // given
    Long userId = 1L;

    // when
    Set<Long> actual = repository.findByUserIdQuery(userId);

    // then
    assertNotNull(actual);
    assertFalse(actual.isEmpty());
    assertEquals(8, actual.size());
  }

  @Test
  void givenIncorrectUserId_whenCallFindByUserIdQuery_thenReturnEmpty() {
    // given
    Long userId = 100L;

    // when
    Set<Long> actual = repository.findByUserIdQuery(userId);

    // then
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }

  @Test
  void givenNullUserId_whenCallFindByUserIdQuery_thenReturnEmpty() {
    // when
    Set<Long> actual = repository.findByUserIdQuery(null);

    // then
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }
}
