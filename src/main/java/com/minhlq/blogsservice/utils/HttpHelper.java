package com.minhlq.blogsservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
@RequiredArgsConstructor
public class HttpHelper {

  private final RestTemplate restTemplate;

  private final ObjectMapper mapper;

  public <T> T toObject(String content, Class<T> valueType) throws IOException {
    return mapper.readValue(content, valueType);
  }

  public <T> List<T> toObjects(String content, Class<T> valueType) throws IOException {
    return mapper.readValue(
        content, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
  }

  private <T> Map<String, T> toMap(String content, Class<T> valueType) throws IOException {
    return mapper.readValue(
        content, mapper.getTypeFactory().constructMapType(Map.class, String.class, valueType));
  }

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
