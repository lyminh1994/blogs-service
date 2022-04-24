package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:db/users.sql")
class UserRepositoryTest extends BaseRepositoryTest {

  @Autowired UserRepository userRepository;

  @Test
  @DisplayName("Should Find By Username Success")
  void shouldFindByUsernameSuccess() {
    String username = "username1";
    Optional<UserEntity> actual = userRepository.findByUsername(username);

    assertTrue(actual.isPresent());
    assertEquals(username, actual.get().getUsername());
  }

  @Test
  @DisplayName("Should Find By Email Success")
  void shouldFindByEmailSuccess() {
    String email = "email1@example.com";
    Optional<UserEntity> actual = userRepository.findByEmail(email);

    assertTrue(actual.isPresent());
    assertEquals(email, actual.get().getEmail());
  }
}
