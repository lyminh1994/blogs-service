package com.minhlq.blogsservice.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public JPAQueryFactory queryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }
}
