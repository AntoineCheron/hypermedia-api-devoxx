package com.github.antoinecheron.hypermedia.noannotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.typesafe.config.ConfigFactory;
import org.springframework.core.codec.Decoder;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import com.github.antoinecheron.hypermedia.noannotation.product.Product;
import com.github.antoinecheron.hypermedia.noannotation.product.ProductDeserializer;
import com.github.antoinecheron.hypermedia.noannotation.product.ProductWithoutId;
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
          final Decoder<?> jacksonDecoder = new Jackson2JsonDecoder(Jackson.configureCustomObjectMapper(),
            MimeTypeUtils.APPLICATION_JSON, MimeType.valueOf("application/*+json")
          );

          serverCodecConfigurer.defaultCodecs().jackson2JsonDecoder(jacksonDecoder);
        })
        .build();
    }

  }

  public static class Jackson {

    /**
     * Configure the jackson object mapper. It contains serializers and deserializers.
     *
     * @return an ObjectMapper configured to suit this application's needs.
     */
    private static ObjectMapper configureCustomObjectMapper() {
      return Jackson2ObjectMapperBuilder.json().deserializers(
        new ProductDeserializer(),
        new ProductWithoutIdDeserializer()
      ).build();
    }

  }

}
