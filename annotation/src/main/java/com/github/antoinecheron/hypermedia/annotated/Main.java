package com.github.antoinecheron.hypermedia.annotated;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.antoinecheron.hypermedia.resource.EnableResourceAbstraction;

@SpringBootApplication
@EnableResourceAbstraction
public class Main {

  private final EmbeddedMongo embeddedMongo;

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  public Main(EmbeddedMongo embeddedMongo) {
    this.embeddedMongo = embeddedMongo;
  }

  @PostConstruct
  public void init() {
    embeddedMongo.run();
  }

}
