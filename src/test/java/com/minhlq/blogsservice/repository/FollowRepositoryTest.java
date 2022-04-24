package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:db/follows.sql")
class FollowRepositoryTest extends BaseRepositoryTest {

  @Autowired FollowRepository followRepository;

  @Test
  @DisplayName("Should Find Followed Users Success")
  void shouldFindFollowedUsersSuccess() {
    Long userId = 1L;
    Set<Long> actual = followRepository.findFollowedUsers(userId);

    assertNotNull(actual);
    assertEquals(2, actual.size());
  }
}
