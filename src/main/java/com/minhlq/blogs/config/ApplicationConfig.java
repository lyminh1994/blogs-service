package com.minhlq.blogs.config;

import com.minhlq.blogs.service.impl.DateTimeProviderImpl;
import java.time.Clock;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    var bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());

    return bean;
  }
}
