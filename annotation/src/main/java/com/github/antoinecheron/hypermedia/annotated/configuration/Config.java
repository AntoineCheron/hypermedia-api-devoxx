package com.github.antoinecheron.hypermedia.annotated.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Config {

  private Config() {}

  public static final Scheduler APPLICATION_SCHEDULER = Schedulers.elastic();

  public static final ObjectMapper JACKSON_OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

}
