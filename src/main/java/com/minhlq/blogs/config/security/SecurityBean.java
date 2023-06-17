package com.minhlq.blogs.config.security;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.constant.SecurityConstants;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
    var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    var jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * Configures cors for all requests towards the API.
   *
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource(final CorsProperties props) {
    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(props.getAllowedOrigins());
    corsConfiguration.setAllowedMethods(props.getAllowedMethods());
    corsConfiguration.setAllowedHeaders(props.getAllowedHeaders());
    corsConfiguration.setAllowCredentials(props.isAllowCredentials());
    corsConfiguration.setMaxAge(props.getMaxAge());
    corsConfiguration.setExposedHeaders(props.getExposedHeaders());

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(AppConstants.ALL_PATTERN_ENDPOINT, corsConfiguration);
    return source;
  }

  /**
   * Enables support for legacy cookie processing.
   *
   * @return WebServerFactoryCustomizer
   */
  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    return serverFactory ->
        serverFactory.addContextCustomizers(
            context -> context.setCookieProcessor(new Rfc6265CookieProcessor()));
  }
}
