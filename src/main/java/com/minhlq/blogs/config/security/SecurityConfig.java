package com.minhlq.blogs.config.security;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class holds security configuration settings for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;

  private final UserDetailsService userDetailsService;

  private final JwtRequestFilter jwtRequestFilter;

  /**
   * Configures global user's with authentication credentials.
   *
   * @param auth to easily build in memory, LDAP, and JDBC authentication
   * @throws Exception if an error occurs
   */
  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  /**
   * Instantiates AuthenticationManager bean from AuthenticationConfiguration.
   *
   * @param authConfig global auth config
   * @return configured AuthenticationManager
   * @throws Exception if an error occurs
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically, subclasses should not
   * call super as it may override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults());
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .requestMatchers(SecurityConstants.PUBLIC_MATCHERS.toArray(String[]::new))
                .permitAll()
                .requestMatchers(HttpMethod.GET, AppConstants.ARTICLES + AppConstants.FEEDS)
                .authenticated()
                .requestMatchers(
                    HttpMethod.GET,
                    AppConstants.ARTICLES + AppConstants.ALL_PATTERN,
                    AppConstants.USER + AppConstants.USERNAME,
                    AppConstants.TAGS)
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.exceptionHandling(
        exception ->
            exception
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()));

    return http.build();
  }
}
