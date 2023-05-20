package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.SecurityConstants;
import com.minhlq.blogs.enums.TokenType;
import com.minhlq.blogs.handler.exception.SecurityException;
import com.minhlq.blogs.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    var keyBytes = Base64.getDecoder().decode(secret);
    var key = Keys.hmacShaKeyFor(keyBytes);
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
      var keyBytes = Base64.getDecoder().decode(secret);
      var key = Keys.hmacShaKeyFor(keyBytes);
      var jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

      return jwtParser.parseClaimsJws(jwt).getBody();
    } catch (ExpiredJwtException ex) {
      throw new SecurityException("JWT is expired!");
    } catch (UnsupportedJwtException ex) {
      throw new SecurityException("JWT is unsupported!");
    } catch (MalformedJwtException ex) {
      throw new SecurityException("Invalid JWT!");
    } catch (SignatureException e) {
      throw new SecurityException("Invalid JWT signature!");
    } catch (IllegalArgumentException ex) {
      throw new SecurityException("JWT claims string is empty!");
    }
  }

  @Override
  public boolean isValidJwtToken(String jwt) {
    try {
      var keyBytes = Base64.getDecoder().decode(secret);
      var key = Keys.hmacShaKeyFor(keyBytes);
      var jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

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
    var headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.isNotBlank(headerAuth) && headerAuth.startsWith(SecurityConstants.BEARER)) {
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
    var cookies = request.getCookies();
    if (Objects.nonNull(cookies)) {
      return Arrays.stream(cookies)
          .filter(cookie -> StringUtils.equals(TokenType.ACCESS.getName(), cookie.getName()))
          .findFirst()
          .map(Cookie::getValue)
          .orElse(null);
    }

    return null;
  }
}
