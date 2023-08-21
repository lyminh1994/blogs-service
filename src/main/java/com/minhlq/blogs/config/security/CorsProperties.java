package com.minhlq.blogs.config.security;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * CORS configuration properties groups all properties prefixed with "cors.".
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Configuration
public class CorsProperties {

  @Value("${cors.max-age}")
  private Long maxAge;

  @Value("${cors.allow-credentials}")
  private boolean allowCredentials;

  @Value("${cors.allowed-origins}")
  private List<String> allowedOrigins;

  @Value("${cors.allowed-methods}")
  private List<String> allowedMethods;

  @Value("${cors.allowed-headers}")
  private List<String> allowedHeaders;

  @Value("${cors.exposed-headers}")
  private List<String> exposedHeaders;
}
