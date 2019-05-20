package com.github.antoinecheron.hypermedia.notannotated.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.Config;

public class JsonWriter {

  private static final ObjectMapper JSON = Config.Jackson.customObjectMapper;

  public static Mono<String> write(Object value) {
    return Mono.fromCallable(() -> JsonWriter.JSON.writeValueAsString(value));
  }

}
