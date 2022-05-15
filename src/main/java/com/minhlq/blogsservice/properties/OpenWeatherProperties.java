package com.minhlq.blogsservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.open-weather")
public class OpenWeatherProperties {

  private String baseUrl;

  private String apiKey;
}
