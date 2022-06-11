package com.minhlq.blogsservice.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration serializer for {@link ErrorResource}.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public class ErrorResourceSerializer extends JsonSerializer<ErrorResource> {

  @Override
  public void serialize(
      ErrorResource value, JsonGenerator generator, SerializerProvider serializers)
      throws IOException {
    Map<String, List<String>> json = new HashMap<>();
    generator.writeStartObject();
    generator.writeObjectFieldStart("errors");
    value
        .getFieldErrors()
        .forEach(
            fieldErrorResource -> {
              if (!json.containsKey(fieldErrorResource.getField())) {
                json.put(fieldErrorResource.getField(), new ArrayList<>());
              }
              json.get(fieldErrorResource.getField()).add(fieldErrorResource.getMessage());
            });
    for (Map.Entry<String, List<String>> pair : json.entrySet()) {
      generator.writeArrayFieldStart(pair.getKey());
      pair.getValue()
          .forEach(
              content -> {
                try {
                  generator.writeString(content);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
      generator.writeEndArray();
    }
    generator.writeEndObject();
    generator.writeEndObject();
  }
}
