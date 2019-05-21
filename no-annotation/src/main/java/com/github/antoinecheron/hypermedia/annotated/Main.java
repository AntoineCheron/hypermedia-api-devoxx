package com.github.antoinecheron.hypermedia.annotated;

import java.io.IOException;
import java.time.Duration;
import java.util.function.BiConsumer;

import com.github.antoinecheron.hypermedia.annotated.configuration.MongoConfiguration;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.netty.http.server.HttpServer;

import com.github.antoinecheron.hypermedia.annotated.process.ProcessApi;
import com.github.antoinecheron.hypermedia.annotated.process.ProcessRepository;
import com.github.antoinecheron.hypermedia.annotated.process.ReactiveMongoProcessRepository;
import com.github.antoinecheron.hypermedia.annotated.product.ReactiveMongoProductRepository;
import com.github.antoinecheron.hypermedia.annotated.product.ProductApi;
import com.github.antoinecheron.hypermedia.annotated.product.ProductRepository;

public class Main {

  public static void main(String[] args) {
    startEmbeddedMongo((dbHost, dbPort) -> {
      final var database = new MongoConfiguration().getMongoOperations(dbHost, dbPort);

      final ProductRepository productService = new ReactiveMongoProductRepository(database);
      final ProcessRepository processService = new ReactiveMongoProcessRepository(database);

      final var routerFunction = RouterFunctions
        .nest(RequestPredicates.path("/products"), new ProductApi(productService).routerFunction)
        .andNest(RequestPredicates.path("/process"), new ProcessApi(processService).routerFunction);

      startHttpServer(routerFunction);
    });

  }

  private static void startEmbeddedMongo(BiConsumer<String, Integer> callback) {
    MongodStarter starter = MongodStarter.getDefaultInstance();
    MongodExecutable mongodExecutable = null;

    String bindIp = "localhost";
    int port = 12345;

    try {
      IMongodConfig mongodConfig = new MongodConfigBuilder()
          .version(Version.Main.PRODUCTION)
          .net(new Net(bindIp, port, Network.localhostIsIPv6()))
          .build();

      mongodExecutable = starter.prepare(mongodConfig);
      mongodExecutable.start();

      callback.accept(bindIp, port);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (mongodExecutable != null)
        mongodExecutable.stop();
    }
  }

  private static void startHttpServer(RouterFunction<ServerResponse> routerFunction) {
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction, Config.Spring.handlerStrategies());
    final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create()
        .host(Config.HOST)
        .port(Config.PORT)
        .handle(adapter)
        .bindUntilJavaShutdown(Duration.ofSeconds(45), (disposableServer) ->
          System.out.println(
              Thread.currentThread().toString()
                  + " Server started on "
                  + disposableServer.host()
                  + ":" + disposableServer.port()
          )
        );
  }

}
