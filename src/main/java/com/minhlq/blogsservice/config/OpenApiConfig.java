package com.minhlq.blogsservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${springdoc.version}")
  private String version;

  @Value("${springdoc.description}")
  private String description;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .components(new Components().addSecuritySchemes("app_auth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
            .info(new Info()
                    .title("Blogs API")
                    .version(version)
                    .description(description)
                    .termsOfService("http://swagger.io/terms/")
                    .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            );
  }

}
