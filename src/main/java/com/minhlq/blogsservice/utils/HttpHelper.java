package com.minhlq.blogsservice.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpHelper {

  private final RestTemplate restTemplate;

  public String execute(String url, HttpMethod method) {
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, null, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return StringUtils.EMPTY;
  }

  public String execute(String url, HttpMethod method, Object body) {
    HttpEntity<Object> requestEntity = HttpEntityUtils.entity(StringUtils.EMPTY, body);
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(url, method, requestEntity, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return StringUtils.EMPTY;
  }

  public String execute(String url, Object body) {
    HttpEntity<Object> requestEntity =
        HttpEntityUtils.entity(StringUtils.EMPTY, body, MediaType.MULTIPART_FORM_DATA);
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    return StringUtils.EMPTY;
  }
}
