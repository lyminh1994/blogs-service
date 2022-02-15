package com.minhlq.blogsservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private String secret;

  private long sessionTime;
}
