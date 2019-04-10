package fr.cheron.antoine.hypermedia.noannotation;

import java.io.IOException;
import java.util.function.BiConsumer;

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

import fr.cheron.antoine.hypermedia.noannotation.rest.ProductApi;
import fr.cheron.antoine.hypermedia.noannotation.repositories.ProductRepository;

public class Main {

  public static void main(String[] args) {
    startEmbeddedMongo((dbHost, dbPort) -> {
      final ProductRepository productService = null;

      final var routerFunction = RouterFunctions.
          nest(RequestPredicates.path("/products"), new ProductApi(productService).routerFunction);

      // startHttpServer(routerFunction);
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
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
    final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create()
        .host(Config.HOST)
        .port(Config.PORT)
        .handle(adapter)
        .bind().block();
  }

}
