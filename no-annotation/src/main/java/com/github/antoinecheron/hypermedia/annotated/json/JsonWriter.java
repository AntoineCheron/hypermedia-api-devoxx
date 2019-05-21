package com.github.antoinecheron.hypermedia.annotated.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.annotated.Config;

public class JsonWriter {

  private static final ObjectMapper JSON = Config.Jackson.customObjectMapper;

  public static Mono<String> write(Object value) {
    try { return Mono.just(JsonWriter.JSON.writeValueAsString(value)); }
    catch (JsonProcessingException e) { return Mono.error(e); }
  }

}
