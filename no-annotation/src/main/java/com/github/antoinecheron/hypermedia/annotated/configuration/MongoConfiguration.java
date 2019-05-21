package com.github.antoinecheron.hypermedia.annotated.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

public class MongoConfiguration {

  private static final String DATABASE_NAME = "hypermedia-example";

  public MongoClient getMongoClient(String host, int port) {

    final var settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString("mongodb://" + host + ":" + port))
      .build();

    return MongoClients.create(settings);
  }

  public ReactiveMongoOperations getMongoOperations(String host, int port) {
    return new ReactiveMongoTemplate(getMongoClient(host, port), DATABASE_NAME);
  }

}
