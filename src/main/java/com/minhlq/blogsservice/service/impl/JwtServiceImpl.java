package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.exception.SecurityException;
import com.minhlq.blogsservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the jwt service.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.config.secret}")
  private String secret;

  @Value("${jwt.config.ttl}")
  private Long ttl;

  @Override
  public String createJwt(String username) {
    return createJwt(username, Date.from(Instant.now().plusMillis(ttl)));
  }

  @Override
  public String createJwt(String username, Date expiration) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    Key key = Keys.hmacShaKeyFor(keyBytes);
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expiration)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Retrieve Jwt claims from the token.
   *
   * @param jwt the token
   * @return the claims
   */
  private Claims parseJwt(String jwt) {
    try {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      Key key = Keys.hmacShaKeyFor(keyBytes);
      JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

      return jwtParser.parseClaimsJws(jwt).getBody();
    } catch (ExpiredJwtException ex) {
      log.error("JWT is expired: {}", ex.getMessage());
      throw new SecurityException("JWT is expired!");
    } catch (UnsupportedJwtException ex) {
      log.error("JWT is unsupported: {}", ex.getMessage());
      throw new SecurityException("JWT is unsupported!");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT: {}", ex.getMessage());
      throw new SecurityException("Invalid JWT!");
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
      throw new SecurityException("Invalid JWT signature!");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty: {}", ex.getMessage());
      throw new SecurityException("JWT claims string is empty!");
    }
  }

  @Override
  public boolean isValidJwtToken(String jwt) {
    try {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      Key key = Keys.hmacShaKeyFor(keyBytes);
      JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

      jwtParser.parseClaimsJws(jwt);
      return true;
    } catch (ExpiredJwtException ex) {
      log.error("JWT token is expired: {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      log.error("JWT token is unsupported: {}", ex.getMessage());
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token: {}", ex.getMessage());
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature: {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty: {}", ex.getMessage());
    }

    return false;
  }

  @Override
  public String getUsernameFromJwt(String jwt) {
    return parseJwt(jwt).getSubject();
  }

  @Override
  public String getJwtToken(HttpServletRequest request, boolean fromCookie) {
    if (fromCookie) {
      return getJwtFromCookie(request);
    }

    return getJwtFromRequest(request);
  }

  /**
   * Retrieves the jwt token from the request header if present and valid.
   *
   * @param request the httpRequest
   * @return the jwt token
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.isNotBlank(headerAuth)
        && headerAuth.startsWith(SecurityConstants.BEARER_PREFIX)) {
      return headerAuth.split(StringUtils.SPACE)[1];
    }

    return null;
  }

  /**
   * Retrieves the jwt token from the request cookie if present and valid.
   *
   * @param request the httpRequest
   * @return the jwt token
   */
  private String getJwtFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (Objects.nonNull(cookies)) {
      for (Cookie cookie : cookies) {
        if (TokenType.ACCESS.getName().equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }
}
