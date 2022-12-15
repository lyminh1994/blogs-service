package com.minhlq.blogsservice.config.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;

/**
 * This class holds JPA configurations for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class JpaConfig {

    /**
     * AuditorAware bean used for auditing.
     *
     * @return Application implementation of AuditorAware.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }

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
