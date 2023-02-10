package com.minhlq.blogsservice.config;

import com.minhlq.blogsservice.service.impl.DateTimeProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.Clock;

/**
 * This class holds application configuration settings for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {

  /**
   * A bean to be used by Clock.
   *
   * @return instance of Clock
   */
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  /**
   * A bean to be used by DateTimeProvider.
   *
   * @return instance of CurrentDateTimeProvider
   */
  @Bean
  @Primary
  public DateTimeProvider dateTimeProvider() {
    return new DateTimeProviderImpl(clock());
  }
}
