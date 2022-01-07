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

  public static final String APP_AUTH = "app_auth";

  public static final String BEARER = "bearer";

  public static final String JWT = "JWT";

  @Value("${springdoc.version}")
  private String version;

  @Value("${springdoc.description}")
  private String description;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .components(new Components()
                    .addSecuritySchemes(APP_AUTH, new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(BEARER)
                            .bearerFormat(JWT)))
            .info(new Info()
                    .title("Blogs API")
                    .version(version)
                    .description(description)
                    .termsOfService("http://swagger.io/terms/")
                    .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }

}
