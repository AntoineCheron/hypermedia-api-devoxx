package com.github.antoinecheron.hypermedia.noannotation;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.typesafe.config.ConfigFactory;
import org.springframework.core.codec.Decoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import com.github.antoinecheron.hypermedia.noannotation.process.ProcessCreationFormDeserializer;
import com.github.antoinecheron.hypermedia.noannotation.process.ProcessDeserializer;
import com.github.antoinecheron.hypermedia.noannotation.product.ProductDeserializer;
import com.github.antoinecheron.hypermedia.noannotation.product.ProductWithoutIdDeserializer;

public class Config {

  public static final com.typesafe.config.Config configuration = ConfigFactory.defaultApplication().resolve();

  public static final String HOST = configuration.getString("http.host");
  public static final int PORT = configuration.getInt("http.port");

  public static final Scheduler APPLICATION_SCHEDULER = Schedulers.elastic();

  public static class Spring {

    public static HandlerStrategies handlerStrategies() {
      return HandlerStrategies.builder()
        .codecs((serverCodecConfigurer) -> {
          final Decoder<?> jacksonDecoder = new Jackson2JsonDecoder(Jackson.customObjectMapper,
            MimeTypeUtils.APPLICATION_JSON, MimeType.valueOf("application/*+json")
          );

          serverCodecConfigurer.defaultCodecs().jackson2JsonDecoder(jacksonDecoder);
        })
        .build();
    }

  }

  public static class Jackson {

    /**
     * Configure the jackson object mapper to suit this application's needs. It contains serializers and deserializers.
     */
    public static final ObjectMapper customObjectMapper = Jackson2ObjectMapperBuilder.json()
      .deserializers(
        new ProductDeserializer(),
        new ProductWithoutIdDeserializer(),
        new ProcessDeserializer(),
        new ProcessCreationFormDeserializer()
      )
      .build();

  }

}
