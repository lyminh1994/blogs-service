package com.minhlq.blogs.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorResourceSerializer extends JsonSerializer<ErrorsResource> {

  @Override
  public void serialize(ErrorsResource value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    var json = new HashMap<String, List<String>>();
    gen.writeStartObject();
    gen.writeObjectFieldStart("errors");
    for (var fieldErrorResource : value.fieldErrors()) {
      if (!json.containsKey(fieldErrorResource.field())) {
        json.put(fieldErrorResource.field(), new ArrayList<>());
      }
      json.get(fieldErrorResource.field()).add(fieldErrorResource.message());
    }
    for (var pair : json.entrySet()) {
      gen.writeArrayFieldStart(pair.getKey());
      pair.getValue()
          .forEach(
              content -> {
                try {
                  gen.writeString(content);
                } catch (IOException e) {
                  log.error("Write object to string error", e);
                }
              });
      gen.writeEndArray();
    }
    gen.writeEndObject();
    gen.writeEndObject();
  }
}
