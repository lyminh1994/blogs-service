package com.minhlq.blogsservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ObjectMapperHelper {

  private final ObjectMapper mapper;

  /**
   * Parse to json object from string
   *
   * @param content Value
   * @param valueType Java type of object
   * @return Object
   * @param <T> Generic object
   */
  public <T> T toObject(String content, Class<T> valueType) {
    try {
      return mapper.readValue(content, valueType);
    } catch (IOException ex) {
      log.error("Read value from String to {} error:", valueType, ex);
      throw new IllegalArgumentException();
    }
  }

  /**
   * Parse to json list from string
   *
   * @param content Value
   * @param valueType Java type of list value
   * @return List
   * @param <T> Generic object
   */
  public <T> List<T> toList(String content, Class<T> valueType) {
    try {
      return mapper.readValue(
          content, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
    } catch (IOException ex) {
      log.error("Read value from String to List<{}> error:", valueType, ex);
      throw new IllegalArgumentException();
    }
  }

  /**
   * Parse to json map from string
   *
   * @param content Value
   * @param valueType Java type of map value
   * @return Map
   * @param <T> Generic object
   */
  public <T> Map<String, T> toMap(String content, Class<T> valueType) {
    try {
      return mapper.readValue(
          content, mapper.getTypeFactory().constructMapType(Map.class, String.class, valueType));
    } catch (IOException ex) {
      log.error("Read value from String to Map<String, {}> error:", valueType, ex);
      throw new IllegalArgumentException();
    }
  }

  /**
   * Write java object to string
   *
   * @param value Object value
   * @return string
   */
  public String writeValueAsString(Object value) {
    try {
      return mapper.writeValueAsString(value);
    } catch (IOException ex) {
      log.error("Write {} to String error:", value, ex);
      throw new IllegalArgumentException();
    }
  }
}
