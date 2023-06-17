package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.handler.exception.SecurityException;
import com.minhlq.blogs.service.JwtService;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
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

  private final JwtEncoder jwtEncoder;

  private final JwtDecoder jwtDecoder;

  @Value("${jwt.config.ttl}")
  private Long ttl;

  @Override
  public String createJwt(String username) {
    return createJwt(username, Instant.now().plusMillis(ttl));
  }

  @Override
  public String createJwt(String username, Instant expiration) {
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .subject(username)
            .issuedAt(Instant.now())
            .expiresAt(expiration)
            .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  @Override
  public boolean isValidJwtToken(String token) {
    try {
      jwtDecoder.decode(token);
      return true;
    } catch (JwtException ex) {
      log.error(ex.getMessage());
    }

    return false;
  }

  @Override
  public String getUsernameFromJwt(String jwt) {
    return parseJwt(jwt).getOrDefault(JwtClaimNames.SUB, StringUtils.EMPTY).toString();
  }

  /**
   * Retrieve Jwt claims from the token.
   *
   * @param token the token
   * @return the claims
   */
  private Map<String, Object> parseJwt(String token) {
    try {
      Jwt jwt = jwtDecoder.decode(token);
      return jwt.getClaims();
    } catch (JwtException ex) {
      throw new SecurityException(ex.getMessage());
    }
  }
}
