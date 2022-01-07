package com.minhlq.blogsservice.config;

import com.minhlq.blogsservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.emptyList;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    String jwt = jwtService.getJwtFromRequest(request);
    if (StringUtils.isNoneBlank(jwt)) {
      String username = jwtService.getUsernameFromJwt(jwt);
      UserDetails user = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(user, null, emptyList());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }

}
