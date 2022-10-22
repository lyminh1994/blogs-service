package com.minhlq.blogsservice.config;

import static com.minhlq.blogsservice.constant.SecurityConstants.AUTH_ROOT_URL;
import static org.springdoc.core.Constants.ALL_PATTERN;
import static org.springdoc.core.Constants.HEALTH_PATTERN;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
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
public class OpenApiConfig {

  public static final String BEARER_AUTH = "bearerAuth";

  /**
   * Configures the actuator group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  @Profile("!prod")
  public GroupedOpenApi actuatorApi(
      @Value("${application.version}") String version,
      OpenApiCustomiser actuatorOpenApiCustomiser,
      OperationCustomizer actuatorCustomizer,
      WebEndpointProperties endpointProperties) {
    return GroupedOpenApi.builder()
        .group("Actuator")
        .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
        .addOpenApiCustomiser(actuatorOpenApiCustomiser)
        .addOpenApiCustomiser(
            openApi -> openApi.info(new Info().title("Actuator APIs").version(version)))
        .addOperationCustomizer(actuatorCustomizer)
        .pathsToExclude(endpointProperties.getBasePath() + HEALTH_PATTERN)
        .build();
  }

  /**
   * Configures the authentication group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  public GroupedOpenApi customOpenApi(
      @Value("${application.title}") String name,
      @Value("${application.version}") String version,
      @Value("${application.description}") String description,
      WebEndpointProperties endpointProperties) {
    return GroupedOpenApi.builder()
        .group("Blogs")
        .addOpenApiCustomiser(
            openApi ->
                openApi.info(
                    new Info()
                        .title(name)
                        .version(version)
                        .description(description)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))))
        .pathsToExclude(endpointProperties.getBasePath() + ALL_PATTERN, AUTH_ROOT_URL + ALL_PATTERN)
        .build();
  }

  @Bean
  public OpenAPI customizeOpenAPISecurity() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
        .components(
            new Components()
                .addSecuritySchemes(
                    BEARER_AUTH,
                    new SecurityScheme()
                        .name(BEARER_AUTH)
                        .type(Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
