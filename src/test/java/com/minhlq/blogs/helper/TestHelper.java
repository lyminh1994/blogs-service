package com.minhlq.blogs.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class TestHelper {

  public static final ObjectMapper objectMapper = new ObjectMapper();

  public static final String[] IGNORED_FIELDS = {
    "id", "createdAt", "createdBy", "updatedAt", "updatedBy"
  };

  public static final String[] BASE_EQUALS_AND_HASH_CODE_FIELDS = {"version", "publicId"};

  public static final String[] USER_EQUALS_FIELDS = {"publicId", "username", "email"};

  public static Collection<String> getBaseEqualsAndHashCodeFields() {
    return List.of(BASE_EQUALS_AND_HASH_CODE_FIELDS);
  }

  public static Collection<String> getIgnoredFields() {
    return List.of(IGNORED_FIELDS);
  }

  public static String[] getEntityEqualsFields(String... fields) {
    return ArrayUtils.addAll(getBaseEqualsAndHashCodeFields().toArray(new String[0]), fields);
  }

  public static Collection<String> getUserEqualsFields() {
    String[] userEquals = ArrayUtils.addAll(BASE_EQUALS_AND_HASH_CODE_FIELDS, USER_EQUALS_FIELDS);
    return List.of(userEquals);
  }

  /**
   * Converts an object to JSON string.
   *
   * @param object the object
   * @param <T> the type of object passed
   * @return the json string
   */
  public static <T> String toJson(T object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }

  /**
   * Parse a JSON string to an object.
   *
   * @param content the content to be parsed
   * @param classType the class to be returned
   * @param <T> the class type
   * @return the parsed object
   */
  public static <T> T parse(String content, Class<T> classType) throws IOException {
    return objectMapper.readValue(content, classType);
  }
}
