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

/**
 * Utility class handle redis operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class RedisHelper {

  private final StringRedisTemplate redisTemplate;

  private final ObjectMapper objectMapper;

  /**
   * Check key existed in redis.
   *
   * @param key the key
   * @return is existed
   */
  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  /**
   * Get string value from provide key.
   *
   * @param key the key
   * @return value
   */
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  /**
   * Get value from provide return value type.
   *
   * @param key the key
   * @param valueType return value type
   * @return value
   * @throws IOException Read value to value type errors
   */
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

  /**
   * Set value to redis.
   *
   * @param key the key
   * @param value the value
   * @throws IOException Write value to string errors
   */
  public void set(String key, Object value) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString);
  }

  /**
   * Set value to redis with provide timeout.
   *
   * @param key the key
   * @param value the value
   * @param timeout the timeout
   * @param unit time unit {@link TimeUnit}
   * @throws IOException Write value to string errors
   */
  public void set(String key, Object value, long timeout, TimeUnit unit) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout, unit);
  }

  /**
   * Set value to redis with provide timeout.
   *
   * @param key the key
   * @param value the value
   * @param timeout the timeout {@link Duration}
   * @throws IOException Write value to string errors
   */
  public void set(String key, Object value, Duration timeout) throws IOException {
    String valueAsString = objectMapper.writeValueAsString(value);
    redisTemplate.opsForValue().set(key, valueAsString, timeout);
  }

  /**
   * Delete redis with provide key.
   *
   * @param key the key
   */
  public void del(String key) {
    redisTemplate.delete(key);
  }
}
