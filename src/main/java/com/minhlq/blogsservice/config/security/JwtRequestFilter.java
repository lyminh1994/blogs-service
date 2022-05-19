package com.minhlq.blogsservice.config.security;

import com.minhlq.blogsservice.service.CryptoService;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.util.SecurityUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
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

  private final CryptoService cryptoService;

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    // Get the token from the request header
    String jwt = jwtService.getJwtToken(request, false);

    if (StringUtils.isBlank(jwt)) {
      // if no Authorization token was found from the header, check the cookies.
      jwt = jwtService.getJwtToken(request, true);
    }

    if (StringUtils.isNotBlank(jwt)) {
      String accessToken = cryptoService.decrypt(jwt);

      if (StringUtils.isNotBlank(accessToken) && jwtService.isValidJwtToken(accessToken)) {
        String username = jwtService.getUsernameFromJwt(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        SecurityUtils.authenticateUser(request, userDetails);
      }
    }

    filterChain.doFilter(request, response);
  }
}
