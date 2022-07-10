package com.minhlq.blogsservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.exception.HttpClientException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Utility class handle call for rest api.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class HttpClientHelper {

  private final RestTemplate restTemplate;

  private ObjectMapper objectMapper;

  /**
   * Execute rest api request with empty body.
   *
   * @param url the url
   * @param method the http method
   * @return response body
   */
  public String execute(String url, HttpMethod method) {
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, null, String.class);
    HttpStatus status = responseEntity.getStatusCode();
    String body = responseEntity.getBody();
    if (status.is2xxSuccessful()) {
      return body;
    }

    throw new HttpClientException(status, body);
  }

  /**
   * Execute rest api request with body.
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
   * Execute rest api POST method with request body.
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

  public <T> T getForValue(String url, Class<T> valueType) {
    String response = execute(url, HttpMethod.GET);
    try {
      return objectMapper.readValue(response, valueType);
    } catch (IOException e) {
      throw new HttpClientException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
