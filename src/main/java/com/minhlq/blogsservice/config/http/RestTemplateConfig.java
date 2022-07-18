package com.minhlq.blogsservice.config.http;

import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * This class holds rest template configuration settings for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final CloseableHttpClient httpClient;

  /**
   * A http client bean.
   *
   * @return the httpClient
   */
  @Bean
  public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
        new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setHttpClient(httpClient);
    return clientHttpRequestFactory;
  }

  /**
   * A RestTemplate bean.
   *
   * @return the restTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .requestFactory(this::clientHttpRequestFactory)
        .errorHandler(new HttpClientErrorHandler())
        .interceptors(new HttpClientHeadersInterceptor(), new HttpClientLoggingInterceptor())
        .build();
  }
}
