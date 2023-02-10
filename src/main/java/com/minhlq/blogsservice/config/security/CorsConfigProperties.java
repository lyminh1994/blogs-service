package com.minhlq.blogsservice.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CORS configuration properties groups all properties prefixed with "cors.".
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfigProperties {

  private int maxAge;

  private boolean allowCredentials;

  private List<String> allowedOrigins;

  private List<String> allowedMethods;

  private List<String> allowedHeaders;

  private List<String> exposedHeaders;
}
