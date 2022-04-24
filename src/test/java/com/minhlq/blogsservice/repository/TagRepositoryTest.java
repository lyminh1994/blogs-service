package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minhlq.blogsservice.entity.TagEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:db/tags.sql")
class TagRepositoryTest extends BaseRepositoryTest {
  @Autowired TagRepository tagRepository;

  @Test
  @DisplayName("Should Find By Name Success")
  void shouldFindByNameSuccess() {
    Optional<TagEntity> actual = tagRepository.findByName("tag1");

    assertTrue(actual.isPresent());
    assertEquals("tag1", actual.get().getName());
  }

  @Test
  @DisplayName("Should Find Names Success")
  void shouldFindNamesSuccess() {

    Pageable pageable = PageRequest.of(0, 10);
    Page<String> actual = tagRepository.findNames(pageable);

    assertNotNull(actual);
    assertEquals(Arrays.asList("tag1", "tag2"), actual.getContent());
    assertEquals(2, actual.getTotalElements());
  }
}
