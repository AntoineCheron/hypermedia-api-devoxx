package fr.cheron.antoine.hypermedia.noannotation;

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
    final ProductRepository productService = null;

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

}
