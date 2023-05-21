package com.minhlq.blogs.config.security;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.constant.SecurityConstants;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
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

  @Value("${jwt.config.private-key}")
  private RSAPrivateKey privateKey;

  @Value("${jwt.config.public-key}")
  private RSAPublicKey publicKey;

  /**
   * Creates and configures a bean for the password encoder.
   *
   * @return BcryptPasswordEncoder with security strength 12
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(SecurityConstants.SECURITY_STRENGTH);
  }

  /**
   * Creates and configures a bean for the JWT decoder.
   *
   * @return The configured JwtDecoder bean.
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  /**
   * Creates and configures a bean for the JWT encoder.
   *
   * @return The configured JwtEncoder bean.
   */
  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * Configures cors for all requests towards the API.
   *
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource(final CorsProperties props) {
    var allowedMethods =
        CollectionUtils.isEmpty(props.getAllowedMethods())
            ? SecurityConstants.HTTP_METHODS_ALLOWED
            : props.getAllowedMethods();
    var allowedHeaders =
        CollectionUtils.isEmpty(props.getAllowedHeaders())
            ? SecurityConstants.HTTP_HEADERS_ALLOWED
            : props.getAllowedHeaders();
    var exposedHeaders =
        CollectionUtils.isEmpty(props.getExposedHeaders())
            ? SecurityConstants.HTTP_HEADERS_EXPOSED
            : props.getExposedHeaders();

    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(props.getAllowedOrigins());
    corsConfiguration.setAllowedMethods(allowedMethods);
    corsConfiguration.setMaxAge(Duration.ofHours(props.getMaxAge()));
    corsConfiguration.setAllowedHeaders(allowedHeaders);
    corsConfiguration.setExposedHeaders(exposedHeaders);
    corsConfiguration.setAllowCredentials(props.isAllowCredentials());

    var source = new UrlBasedCorsConfigurationSource();
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
