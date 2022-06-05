package com.minhlq.blogsservice.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class holds JPA configurations for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {

  /**
   * JPAQueryFactory bean used for querydsl.
   *
   * @return the JPAQueryFactory
   */
  @Bean
  public JPAQueryFactory queryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }
}
