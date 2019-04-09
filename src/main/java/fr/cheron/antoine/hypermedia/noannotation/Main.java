package fr.cheron.antoine.hypermedia.noannotation;

import java.io.IOException;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.netty.http.server.HttpServer;

import redis.embedded.RedisServer;

import fr.cheron.antoine.hypermedia.noannotation.rest.ProductApi;
import fr.cheron.antoine.hypermedia.noannotation.repositories.ProductRepository;
import fr.cheron.antoine.hypermedia.noannotation.repositories.redis.implem.RedisProductRepository;

public class Main {

  public static void main(String[] args) {
    startEmbeddedRedis();

    final ProductRepository productService = new RedisProductRepository(Config.getRedisConnectionFactory());

    final RouterFunction<ServerResponse> routerFunction = RouterFunctions.
      nest(RequestPredicates.path("/products"), new ProductApi(productService).routerFunction);

    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
    final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create()
      .host(Config.HOST)
      .port(Config.PORT)
      .handle(adapter)
      .bind().block();
  }

  public static void startEmbeddedRedis() {
    try {
      final var redisServer = new RedisServer();
      redisServer.start();

      Runtime.getRuntime().addShutdownHook(new Thread(redisServer::stop));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
