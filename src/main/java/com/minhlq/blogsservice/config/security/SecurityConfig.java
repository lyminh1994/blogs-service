package com.minhlq.blogsservice.config.security;

import static com.minhlq.blogsservice.constant.SecurityConstants.getPublicMatchers;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;

  private final UserDetailsService userDetailsService;

  private final JwtRequestFilter jwtRequestFilter;

  private final JwtAuthenticationEntryPoint unauthorizedHandler;

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
    http.cors().and().csrf().disable();
    http.httpBasic().disable().formLogin().disable().logout().disable();

    http.authorizeRequests()
        .antMatchers(OPTIONS)
        .permitAll()
        .antMatchers(getPublicMatchers().toArray(new String[] {}))
        .permitAll()
        .antMatchers(GET, "/articles/feeds")
        .authenticated()
        .antMatchers(POST, "/auth/**")
        .permitAll()
        .antMatchers(GET, "/articles/**", "/user/{username}", "/tags")
        .permitAll()
        .anyRequest()
        .authenticated();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler);

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
