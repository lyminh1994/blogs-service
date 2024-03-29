package com.minhlq.blogs.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This class holds Open Api 3 configurations for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

  public static final String TERMS_OF_SERVICE = "https://swagger.io/terms/";
  public static final String LICENSE_NAME = "Apache 2.0";
  public static final String LICENSE_URL = "https://springdoc.org";

  /**
   * Configures the actuator group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  @Profile("!prod")
  public GroupedOpenApi actuatorApi(
      @Value("${springdoc.version}") String version,
      OpenApiCustomizer actuatorOpenApiCustomizer,
      WebEndpointProperties endpointProperties) {
    return GroupedOpenApi.builder()
        .group("Actuator")
        .pathsToMatch(endpointProperties.getBasePath() + Constants.ALL_PATTERN)
        .addOpenApiCustomizer(actuatorOpenApiCustomizer)
        .addOpenApiCustomizer(
            openApi -> openApi.info(new Info().title("Actuator APIs").version(version)))
        .pathsToExclude(endpointProperties.getBasePath() + Constants.HEALTH_PATTERN)
        .build();
  }

  /**
   * Configures the authentication group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  public GroupedOpenApi authenticationOpenApi(
      @Value("${springdoc.title}") String name,
      @Value("${springdoc.version}") String version,
      @Value("${springdoc.description}") String description) {
    return GroupedOpenApi.builder()
        .group("Authentication")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new Info()
                        .title(StringUtils.capitalize(name))
                        .version(version)
                        .description(description)
                        .termsOfService(TERMS_OF_SERVICE)
                        .license(new License().name(LICENSE_NAME).url(LICENSE_URL))))
        .pathsToMatch("/auth/**")
        .build();
  }

  /**
   * Configures the general group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  public GroupedOpenApi generalOpenApi(
      @Value("${springdoc.title}") String name,
      @Value("${springdoc.version}") String version,
      @Value("${springdoc.description}") String description,
      WebEndpointProperties endpointProperties) {
    return GroupedOpenApi.builder()
        .group("Blogs")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new Info()
                        .title(StringUtils.capitalize(name))
                        .version(version)
                        .description(description)
                        .termsOfService(TERMS_OF_SERVICE)
                        .license(new License().name(LICENSE_NAME).url(LICENSE_URL))))
        .pathsToExclude(endpointProperties.getBasePath() + Constants.ALL_PATTERN, "/auth/**")
        .build();
  }

  /**
   * Configures OpenAPI security JWT schema bean
   *
   * @return the {@link OpenAPI} bean
   */
  @Bean
  public OpenAPI customizeOpenApiSecurity() {
    final String bearerAuth = "bearerAuth";
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(bearerAuth))
        .components(
            new Components()
                .addSecuritySchemes(
                    bearerAuth,
                    new SecurityScheme()
                        .name(bearerAuth)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
