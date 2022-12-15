package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.ProfileTypeConstants;
import com.minhlq.blogsservice.constant.SecurityConstants;
import com.minhlq.blogsservice.enums.TokenType;
import com.minhlq.blogsservice.service.CookieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

import static com.minhlq.blogsservice.constant.SecurityConstants.HTTP_COOKIE_CANNOT_BE_NULL;
import static com.minhlq.blogsservice.constant.SecurityConstants.NAME_CANNOT_BE_NULL_OR_EMPTY;
import static com.minhlq.blogsservice.constant.SecurityConstants.TOKEN_CANNOT_BE_NULL_OR_EMPTY;
import static com.minhlq.blogsservice.constant.SecurityConstants.TOKEN_TYPE_CANNOT_BE_NULL;

/**
 * This is the implementation of the cookie service.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {

    private final Environment environment;

    private final Duration duration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

    /**
     * Creates a servlet cookie from spring httpCookie.
     *
     * @param httpCookie the httpCookie
     * @return the cookie
     */
    @Override
    public Cookie createCookie(HttpCookie httpCookie) {
        Validate.notNull(httpCookie, HTTP_COOKIE_CANNOT_BE_NULL);

        Cookie cookie = new Cookie(httpCookie.getName(), httpCookie.getValue());
        cookie.setSecure(
                Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.PROD));
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Creates an httpOnly httpCookie.
     *
     * @param name   the name of the cookie
     * @param value  the value of the cookie
     * @param maxAge the duration till expiration
     * @return the cookie
     */
    @Override
    public HttpCookie createCookie(String name, String value, Duration maxAge) {
        Validate.notBlank(name, NAME_CANNOT_BE_NULL_OR_EMPTY);

        return ResponseCookie.from(name, value)
                .secure(Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.PROD))
                .sameSite("strict")
                .path("/")
                .maxAge(Objects.isNull(maxAge) ? this.duration : maxAge)
                .httpOnly(true)
                .build();
    }

    /**
     * Creates a cookie with the specified token and token type.
     *
     * @param token     the token
     * @param tokenType the type of token
     * @return the cookie
     */
    @Override
    public HttpCookie createTokenCookie(String token, TokenType tokenType) {
        Validate.notBlank(token, TOKEN_CANNOT_BE_NULL_OR_EMPTY);

        return createTokenCookie(token, tokenType, duration);
    }

    /**
     * Creates a cookie with the specified token and token type.
     *
     * @param token     the token
     * @param tokenType the type of token
     * @param maxAge    the duration till expiration
     * @return the cookie
     */
    @Override
    public HttpCookie createTokenCookie(String token, TokenType tokenType, Duration maxAge) {
        Validate.notBlank(token, TOKEN_CANNOT_BE_NULL_OR_EMPTY);
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);

        return createCookie(tokenType.getName(), token, maxAge);
    }

    /**
     * Creates a cookie with the specified token.
     *
     * @param tokenType the token type
     * @return the cookie
     */
    @Override
    public HttpCookie deleteTokenCookie(TokenType tokenType) {
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);

        return createCookie(tokenType.getName(), StringUtils.EMPTY, Duration.ZERO);
    }

    /**
     * Creates a cookie with the specified token.
     *
     * @param tokenType the token type
     * @return the httpHeaders
     */
    @Override
    public HttpHeaders addDeletedCookieToHeaders(TokenType tokenType) {
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);

        HttpCookie httpCookie = deleteTokenCookie(tokenType);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());
        return httpHeaders;
    }

    /**
     * creates a cookie with the specified token and tokenType then adds it to headers.
     *
     * @param tokenType the tokenType
     * @param token     the token
     * @return the httpHeaders
     */
    @Override
    public HttpHeaders addCookieToHeaders(TokenType tokenType, String token) {
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);
        Validate.notBlank(token, TOKEN_CANNOT_BE_NULL_OR_EMPTY);

        return addCookieToHeaders(tokenType, token, duration);
    }

    /**
     * creates a cookie with the specified token and tokenType then adds it to headers.
     *
     * @param tokenType the tokenType
     * @param token     the token
     * @param maxAge    the duration till expiration
     * @return the httpHeaders
     */
    @Override
    public HttpHeaders addCookieToHeaders(TokenType tokenType, String token, Duration maxAge) {
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);
        Validate.notBlank(token, TOKEN_CANNOT_BE_NULL_OR_EMPTY);

        HttpHeaders httpHeaders = new HttpHeaders();
        addCookieToHeaders(
                httpHeaders, tokenType, token, Objects.isNull(maxAge) ? this.duration : maxAge);

        return httpHeaders;
    }

    /**
     * creates a cookie with the specified token and type with duration then adds it to the headers.
     *
     * @param httpHeaders the httpHeaders
     * @param tokenType   the tokenType
     * @param token       the token
     * @param maxAge      the duration till expiration
     */
    @Override
    public void addCookieToHeaders(
            HttpHeaders httpHeaders, TokenType tokenType, String token, Duration maxAge) {
        Validate.notNull(tokenType, TOKEN_TYPE_CANNOT_BE_NULL);

        httpHeaders.add(
                HttpHeaders.SET_COOKIE, createTokenCookie(token, tokenType, maxAge).toString());
    }
}
