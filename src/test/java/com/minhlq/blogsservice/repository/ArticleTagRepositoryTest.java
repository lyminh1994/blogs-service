package com.minhlq.blogsservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minhlq.blogsservice.entity.ArticleTagEntity;
import com.minhlq.blogsservice.helper.BaseRepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:db/article_tags.sql")
class ArticleTagRepositoryTest extends BaseRepositoryTest {

  @Autowired ArticleTagRepository articleTagRepository;

  @Test
  @DisplayName("Should Find By Article Success")
  void shouldFindByArticleSuccess() {
    List<ArticleTagEntity> actual = articleTagRepository.findByArticleId(1L);

    assertNotNull(actual);
    assertEquals(2, actual.size());
  }
}
