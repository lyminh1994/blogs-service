package com.minhlq.blogsservice.config;

import com.minhlq.blogsservice.constant.SecurityConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * This class holds Open Api 3 configurations for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class OpenAPIConfig {

  @Value("${springdoc.name}")
  private String name;

  @Value("${springdoc.version}")
  private String version;

  @Value("${springdoc.description}")
  private String description;

  /**
   * Configures the OpenApi 3.0 bean.
   *
   * @return the OpenApi 3.0 bean
   */
  @Bean
  public OpenAPI customOpenApi() {
    final String apiTitle = String.format("%s APIs", StringUtils.capitalize(name));
    final String securitySchemeName = "bearerAuth";
    final License license = new License().name("Apache 2.0").url("http://springdoc.org");

    Info info =
        new Info()
            .title(apiTitle)
            .version(version)
            .description(description)
            .termsOfService("http://swagger.io/terms/")
            .license(license);

    SecurityScheme securityScheme =
        new SecurityScheme()
            .name(securitySchemeName)
            .type(Type.HTTP)
            .scheme(SecurityConstants.BEARER.toLowerCase())
            .bearerFormat("JWT");
    Components components = new Components().addSecuritySchemes(securitySchemeName, securityScheme);

    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(components)
        .info(info);
  }
}
