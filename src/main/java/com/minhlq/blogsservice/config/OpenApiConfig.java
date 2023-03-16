package com.minhlq.blogsservice.config;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
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

  private final OperationCustomizer actuatorCustomizer;

  /**
   * Configures the actuator group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  @Profile("!prod")
  public GroupedOpenApi actuatorApi(
      @Value("${application.version}") String version,
      OpenApiCustomizer actuatorOpenApiCustomizer,
      WebEndpointProperties endpointProperties) {
    return GroupedOpenApi.builder()
        .group("Actuator")
        .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
        .addOpenApiCustomizer(actuatorOpenApiCustomizer)
        .addOpenApiCustomizer(
            openApi -> openApi.info(new Info().title("Actuator APIs").version(version)))
        .addOperationCustomizer(actuatorCustomizer)
        // .pathsToExclude(endpointProperties.getBasePath() + HEALTH_PATTERN)
        .build();
  }

  /**
   * Configures the authentication group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  public GroupedOpenApi authenticationOpenApi(
      @Value("${application.title}") String name,
      @Value("${application.version}") String version,
      @Value("${application.description}") String description) {
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
        .pathsToMatch("/auth" + ALL_PATTERN)
        .build();
  }

  /**
   * Configures the general group.
   *
   * @return the GroupedOpenApi 3.0 bean
   */
  @Bean
  public GroupedOpenApi generalOpenApi(
      @Value("${application.title}") String name,
      @Value("${application.version}") String version,
      @Value("${application.description}") String description,
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
        .pathsToExclude(endpointProperties.getBasePath() + ALL_PATTERN, "/auth" + ALL_PATTERN)
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
