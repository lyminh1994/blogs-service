package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.constant.SecurityConstants;
import java.time.Duration;
import java.util.Arrays;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
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
  public CorsConfigurationSource corsConfigurationSource(final CorsConfigProperties props) {

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(props.isAllowCredentials());
    corsConfiguration.setMaxAge(Duration.ofHours(props.getMaxAge()));

    setExposedHeaders(props, corsConfiguration);
    setAllowedHeaders(props, corsConfiguration);
    setAllowedMethods(props, corsConfiguration);

    corsConfiguration.setAllowedOrigins(props.getAllowedOrigins());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);

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

  /**
   * Set the list of response headers other than simple headers (i.e. {@code Cache-Control}, {@code
   * Content-Language}, {@code Content-Type}, {@code Expires}, {@code Last-Modified}, or {@code
   * Pragma}) that an actual response might have and can be exposed.
   *
   * <p>The special value {@code "*"} allows all headers to be exposed for non-credentialed
   * requests.
   *
   * <p>By default this is not set.
   *
   * @param props CorsConfigProperties
   * @param corsConfig CorsConfiguration
   */
  private void setExposedHeaders(CorsConfigProperties props, CorsConfiguration corsConfig) {
    if (CollectionUtils.isEmpty(props.getExposedHeaders())) {
      corsConfig.setExposedHeaders(
          Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE));
    } else {
      corsConfig.setExposedHeaders(props.getExposedHeaders());
    }
  }

  /**
   * Set the list of headers that a pre-flight request can list as allowed for use during an actual
   * request.
   *
   * <p>The special value {@code "*"} allows actual requests to send any header.
   *
   * <p>A header name is not required to be listed if it is one of: {@code Cache-Control}, {@code
   * Content-Language}, {@code Expires}, {@code Last-Modified}, or {@code Pragma}.
   *
   * <p>By default this is not set.
   *
   * @param props CorsConfigProperties
   * @param corsConfig CorsConfiguration
   */
  private void setAllowedHeaders(CorsConfigProperties props, CorsConfiguration corsConfig) {
    if (CollectionUtils.isEmpty(props.getAllowedHeaders())) {
      corsConfig.setAllowedHeaders(SecurityConstants.HTTP_HEADERS_ALLOWED);
    } else {
      corsConfig.setAllowedHeaders(props.getAllowedHeaders());
    }
  }

  /**
   * Set the HTTP methods to allow, e.g. {@code "GET"}, {@code "POST"}, {@code "PUT"}, etc.
   *
   * <p>The special value {@code "*"} allows all methods.
   *
   * <p>If not set, only {@code "GET"} and {@code "HEAD"} are allowed.
   *
   * <p>By default this is not set.
   *
   * <p><strong>Note:</strong> CORS checks use values from "Forwarded"
   *
   * <p>(<a href="https://tools.ietf.org/html/rfc7239">RFC 7239</a>), "X-Forwarded-Host",
   * "X-Forwarded-Port", and "X-Forwarded-Proto" headers, if present, in order to reflect the
   * client-originated address. Consider using the {@code ForwardedHeaderFilter} in order to choose
   * from a central place whether to extract and use, or to discard such headers. See the Spring
   * Framework reference for more on this filter.
   *
   * @param props CorsConfigProperties
   * @param corsConfig CorsConfiguration
   */
  private void setAllowedMethods(CorsConfigProperties props, CorsConfiguration corsConfig) {
    if (CollectionUtils.isEmpty(props.getAllowedMethods())) {
      corsConfig.setAllowedMethods(SecurityConstants.HTTP_METHODS_ALLOWED);
    } else {
      corsConfig.setAllowedMethods(props.getAllowedMethods());
    }
  }
}
