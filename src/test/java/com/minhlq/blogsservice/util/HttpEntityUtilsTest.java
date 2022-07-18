package com.minhlq.blogsservice.util;

import com.minhlq.blogsservice.constant.TestConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class HttpEntityUtilsTest {

  @Test
  void shouldReturnBearerAuthInHeadersAndRequestBody() {
    // given
    Object request = ReflectionUtils.newInstance(Object.class);

    // when
    HttpEntity<Object> httpEntity =
        HttpEntityUtils.entity(TestConstants.AUTHENTICATION_TOKEN, request);

    // then
    List<String> headers = httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);
    Assertions.assertNotNull(headers);
    Assertions.assertEquals(TestConstants.BEARER_AUTHENTICATION_TOKEN, headers.get(0));

    Assertions.assertTrue(httpEntity.hasBody());
  }

  @Test
  void shouldReturnBearerAuthAndInputContentTypeInHeadersAndRequestBody() {
    // given
    Object request = ReflectionUtils.newInstance(Object.class);
    MediaType contentType = MediaType.APPLICATION_JSON;

    // when
    HttpEntity<Object> httpEntity =
        HttpEntityUtils.entity(TestConstants.AUTHENTICATION_TOKEN, request, contentType);

    // then
    List<String> headersAuthorization = httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);
    Assertions.assertNotNull(headersAuthorization);
    Assertions.assertEquals(TestConstants.BEARER_AUTHENTICATION_TOKEN, headersAuthorization.get(0));

    List<String> headersContentType = httpEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE);
    Assertions.assertNotNull(headersContentType);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, headersContentType.get(0));

    Assertions.assertTrue(httpEntity.hasBody());
  }

  @Test
  void shouldReturnBearerAuthAndListContentTypeInHeadersAndRequestBody() {
    // given
    Object request = ReflectionUtils.newInstance(Object.class);
    Map<String, String> contentTypes = new HashMap<>();
    contentTypes.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    contentTypes.put(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);

    // when
    HttpEntity<Object> httpEntity =
        HttpEntityUtils.entity(TestConstants.AUTHENTICATION_TOKEN, request, contentTypes);

    // then
    List<String> headersAuthorization = httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);
    Assertions.assertNotNull(headersAuthorization);
    Assertions.assertEquals(TestConstants.BEARER_AUTHENTICATION_TOKEN, headersAuthorization.get(0));

    List<String> headersContentType = httpEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE);
    Assertions.assertNotNull(headersContentType);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, headersContentType.get(0));

    List<String> headersAccept = httpEntity.getHeaders().get(HttpHeaders.ACCEPT);
    Assertions.assertNotNull(headersAccept);
    Assertions.assertEquals(MediaType.ALL_VALUE, headersAccept.get(0));

    Assertions.assertTrue(httpEntity.hasBody());
  }

  @Test
  void shouldReturnBearerAuthInHeadersAndEmptyRequestBody() {
    // when
    HttpEntity<Object> httpEntity = HttpEntityUtils.entity(TestConstants.AUTHENTICATION_TOKEN);

    // then
    List<String> headers = httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);
    Assertions.assertNotNull(headers);
    Assertions.assertEquals(TestConstants.BEARER_AUTHENTICATION_TOKEN, headers.get(0));

    Assertions.assertFalse(httpEntity.hasBody());
  }
}
