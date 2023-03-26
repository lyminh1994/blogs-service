package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.AppConstants;
import com.minhlq.blogsservice.constant.SecurityConstants;
import java.time.Duration;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * This class defines the beans needed for the security operation of the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SecurityBean {

  /**
   * PasswordEncoder bean used in security operations.
   *
   * @return BcryptPasswordEncoder with security strength 12
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(SecurityConstants.SECURITY_STRENGTH);
  }

  /**
   * Configures cors for all requests towards the API.
   *
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource(final CorsProperties props) {
    List<String> allowedMethods =
        CollectionUtils.isEmpty(props.getAllowedMethods())
            ? SecurityConstants.HTTP_METHODS_ALLOWED
            : props.getAllowedMethods();
    List<String> allowedHeaders =
        CollectionUtils.isEmpty(props.getAllowedHeaders())
            ? SecurityConstants.HTTP_HEADERS_ALLOWED
            : props.getAllowedHeaders();
    List<String> exposedHeaders =
        CollectionUtils.isEmpty(props.getExposedHeaders())
            ? SecurityConstants.HTTP_HEADERS_EXPOSED
            : props.getExposedHeaders();

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(props.getAllowedOrigins());
    corsConfiguration.setAllowedMethods(allowedMethods);
    corsConfiguration.setMaxAge(Duration.ofHours(props.getMaxAge()));
    corsConfiguration.setAllowedHeaders(allowedHeaders);
    corsConfiguration.setExposedHeaders(exposedHeaders);
    corsConfiguration.setAllowCredentials(props.isAllowCredentials());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(AppConstants.ALL_PATTERN, corsConfiguration);
    return source;
  }

  /**
   * Enables support for legacy cookie processing.
   *
   * @return WebServerFactoryCustomizer
   */
  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    return tomcatServletWebServerFactory ->
        tomcatServletWebServerFactory.addContextCustomizers(
            context -> context.setCookieProcessor(new Rfc6265CookieProcessor()));
  }
}
