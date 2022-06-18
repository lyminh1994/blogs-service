package com.minhlq.blogsservice.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Utility class handle call for rest api
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class HttpClientHelper {

  private final RestTemplate restTemplate;

  /**
   * Execute rest api request with empty body
   *
   * @param url the url
   * @param method the http method
   * @return response body
   */
  public String execute(String url, HttpMethod method) {
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, null, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return null;
  }

  /**
   * Execute rest api request with body
   *
   * @param url the url
   * @param method the http method
   * @param body the request body
   * @return response body
   */
  public String execute(String url, HttpMethod method, Object body) {
    HttpEntity<Object> requestEntity = HttpEntityUtils.entity(StringUtils.EMPTY, body);
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(url, method, requestEntity, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return null;
  }

  /**
   * Execute rest api POST method with request body
   *
   * @param url the url
   * @param body the request body
   * @return response body
   */
  public String execute(String url, Object body) {
    HttpEntity<Object> requestEntity =
        HttpEntityUtils.entity(StringUtils.EMPTY, body, MediaType.MULTIPART_FORM_DATA);
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return null;
  }
}
