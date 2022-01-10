package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.session-time}")
  private long sessionTime;

  @Override
  public String createJwt(UserPrincipal user) {
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
    } catch (ExpiredJwtException ex) {
      throw new SecurityException("JWT expired", ex);
    } catch (UnsupportedJwtException ex) {
      throw new SecurityException("JWT unsupported", ex);
    } catch (MalformedJwtException ex) {
      throw new SecurityException("Invalid JWT", ex);
    } catch (SignatureException ex) {
      throw new SecurityException("Invalid JWT signature", ex);
    } catch (IllegalArgumentException ex) {
      throw new SecurityException("JWT claims string is empty", ex);
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
