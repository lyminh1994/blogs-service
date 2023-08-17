package com.minhlq.blogs.config.security;

import com.minhlq.blogs.constant.SecurityConstants;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
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
  private Resource privateKey;

  @Value("${jwt.config.public-key}")
  private Resource publicKey;

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
  public JwtDecoder jwtDecoder()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    return NimbusJwtDecoder.withPublicKey(getPublicKey()).build();
  }

  /**
   * Creates and configures a bean for the JWT encoder.
   *
   * @return The configured JwtEncoder bean.
   */
  @Bean
  public JwtEncoder jwtEncoder()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    var jwk = new RSAKey.Builder(getPublicKey()).privateKey(getPrivateKey()).build();
    var jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  private RSAPublicKey getPublicKey()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    var publicKeyBytes = publicKey.getInputStream().readAllBytes();
    var keySpec = new PKCS8EncodedKeySpec(publicKeyBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
  }

  private RSAPrivateKey getPrivateKey()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    var privateKeyBytes = privateKey.getInputStream().readAllBytes();
    var keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
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
    return serverFactory ->
        serverFactory.addContextCustomizers(
            context -> context.setCookieProcessor(new Rfc6265CookieProcessor()));
  }
}
