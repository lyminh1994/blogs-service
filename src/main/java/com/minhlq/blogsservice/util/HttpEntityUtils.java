package com.minhlq.blogsservice.util;

import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@UtilityClass
public final class HttpEntityUtils {

  public HttpEntity<Object> entity(String auth, Object body) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);

    return new HttpEntity<>(body, headers);
  }

  public HttpEntity<Object> entity(String auth, Object body, MediaType contentType) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);
    headers.setContentType(contentType);

    return new HttpEntity<>(body, headers);
  }

  public HttpEntity<Object> entity(String auth, Object body, Map<String, String> contentTypes) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(auth);
    headers.setAll(contentTypes);

    return new HttpEntity<>(body, headers);
  }

  public HttpEntity<Object> entity(String auth) {
    return entity(auth, null);
  }
}
