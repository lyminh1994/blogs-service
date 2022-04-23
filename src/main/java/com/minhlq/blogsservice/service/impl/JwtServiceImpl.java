package com.minhlq.blogsservice.service.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.exception.SecurityException;
import com.minhlq.blogsservice.properties.JwtProperties;
import com.minhlq.blogsservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Instant;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtServiceImpl implements JwtService {

  private final JwtProperties jwtProperties;

  @Override
  public String createJwt(UserPrincipal user) {
    JwtBuilder builder =
        Jwts.builder()
            .setSubject(user.getUsername())
            .setExpiration(Date.from(Instant.now().plusMillis(jwtProperties.getSessionTime())))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret());

    return builder.compact();
  }

  @Override
  public String getUsernameFromJwt(String jwt) {
    return parseJwt(jwt).getSubject();
  }

  private Claims parseJwt(String jwt) {
    try {
      return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(jwt).getBody();
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
