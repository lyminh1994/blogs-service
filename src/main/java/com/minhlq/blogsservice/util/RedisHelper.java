package com.minhlq.blogsservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHelper {

  private final StringRedisTemplate redisTemplate;

  private final ObjectMapper objectMapper;

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> T getObject(String key, Class<T> valueType) throws IOException {
    String value = get(key);
    if (exists(key) && StringUtils.isNotBlank(value)) {
      return objectMapper.readValue(value, valueType);
    }

    return null;
  }

  public <T> List<T> getList(String key, Class<T> valueType) throws IOException {
    String value = get(key);
    if (exists(key) && StringUtils.isNotBlank(value)) {
      return objectMapper.readValue(
          value, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
    }

    return Collections.emptyList();
  }

  public void set(String key, Object value) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString);
  }

  public void set(String key, Object value, long timeout, TimeUnit unit) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout, unit);
  }

  public void set(String key, Object value, Duration timeout) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }
}
