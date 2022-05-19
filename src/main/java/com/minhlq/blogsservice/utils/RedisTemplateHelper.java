package com.minhlq.blogsservice.utils;

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
public class RedisTemplateHelper {

  private final StringRedisTemplate redisTemplate;

  private final ObjectMapperHelper objectMapper;

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> T getObject(String key, Class<T> valueType) {
    String value = get(key);
    if (exists(key) && StringUtils.isNotBlank(value)) {
      return objectMapper.toObject(value, valueType);
    }

    return null;
  }

  public <T> List<T> getList(String key, Class<T> valueType) {
    String value = get(key);
    if (exists(key) && StringUtils.isNotBlank(value)) {
      return objectMapper.toList(value, valueType);
    }

    return Collections.emptyList();
  }

  public void set(String key, Object value) {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString);
  }

  public void set(String key, Object value, long timeout, TimeUnit unit) {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout, unit);
  }

  public void set(String key, Object value, Duration timeout) {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }
}
