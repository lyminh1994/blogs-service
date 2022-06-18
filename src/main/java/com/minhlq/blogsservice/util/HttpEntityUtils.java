package com.minhlq.blogsservice.util;

import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Handle initialize http entity for rest api request.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public final class HttpEntityUtils {

  /**
   * HttpEntity with Bearer auth header and request body.
   *
   * @param auth the authentication token
   * @param body the request body
   * @return entity
   */
  public HttpEntity<Object> entity(String auth, Object body) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);

    return new HttpEntity<>(body, headers);
  }

  /**
   * HttpEntity with Bearer auth header, input content type and request body.
   *
   * @param auth the authentication token
   * @param body the request body
   * @param contentType the content type
   * @return entity
   */
  public HttpEntity<Object> entity(String auth, Object body, MediaType contentType) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);
    headers.setContentType(contentType);

    return new HttpEntity<>(body, headers);
  }

  /**
   * HttpEntity with Bearer auth header, list content type and request body.
   *
   * @param auth the authentication token
   * @param body the request body
   * @param contentTypes list content types
   * @return entity
   */
  public HttpEntity<Object> entity(String auth, Object body, Map<String, String> contentTypes) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);
    headers.setAll(contentTypes);

    return new HttpEntity<>(body, headers);
  }

  /**
   * HttpEntity with Bearer auth header and empty request body.
   *
   * @param auth the authentication token
   * @return entity
   */
  public HttpEntity<Object> entity(String auth) {
    return entity(auth, null);
  }
}
