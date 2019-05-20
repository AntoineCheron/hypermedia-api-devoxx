package com.github.antoinecheron.hypermedia.notannotated.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.github.antoinecheron.hypermedia.notannotated.Config;

public class MongoConfiguration {

  private static MongoClient getMongoClient(String uri) {
    final var settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(uri))
      .build();

    return MongoClients.create(settings);
  }

  public static ReactiveMongoOperations getMongoOperations() {
    return new ReactiveMongoTemplate(getMongoClient(Config.DB_URI), Config.DB_NAME);
  }

}
