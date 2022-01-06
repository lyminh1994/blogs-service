package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.session-time}")
  private long sessionTime;

  @Override
  public String createJwt(User user) {
    JwtBuilder builder = Jwts.builder()
            .setSubject(user.getUsername())
            .setExpiration(Date.from(Instant.now().plusMillis(sessionTime)))
            .signWith(SignatureAlgorithm.HS512, secret);

    return builder.compact();
  }

  @Override
  public String getUsernameFromJwt(String jwt) {
    return parseJwt(jwt).getSubject();
  }

  private Claims parseJwt(String jwt) {
    try {
      return Jwts.parser()
              .setSigningKey(secret)
              .parseClaimsJws(jwt)
              .getBody();
    } catch (Exception e) {
      throw new SecurityException();
    }
  }

  @Override
  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }

}
