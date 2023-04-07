package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.service.EncryptionService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This is a filter base class that is used to guarantee a single execution per request dispatch.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final EncryptionService encryptionService;

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    // Get the token from the request header
    var jwtToken = jwtService.getJwtToken(request, false);

    if (StringUtils.isBlank(jwtToken)) {
      // if no Authorization token was found from the header, check the cookies.
      jwtToken = jwtService.getJwtToken(request, true);
    }

    if (StringUtils.isNotBlank(jwtToken)) {
      var accessToken = encryptionService.decrypt(jwtToken);

      if (StringUtils.isNotBlank(accessToken) && jwtService.isValidJwtToken(accessToken)) {
        var username = jwtService.getUsernameFromJwt(accessToken);
        var userDetails = userDetailsService.loadUserByUsername(username);
        SecurityUtils.authenticateUser(request, userDetails);
      }
    }

    filterChain.doFilter(request, response);
  }
}
