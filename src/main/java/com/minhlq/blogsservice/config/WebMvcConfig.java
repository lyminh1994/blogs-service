package com.minhlq.blogsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class holds web application configuration settings for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${app.static-resource-location}")
  private String staticResourceLocation;

  @Value("${app.static-resource-url}")
  private String staticResourceUrl;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler(staticResourceLocation)
        .addResourceLocations(staticResourceUrl, "classpath:/static/**", "classpath:/templates/**");
    registry
        .addResourceHandler("/swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
