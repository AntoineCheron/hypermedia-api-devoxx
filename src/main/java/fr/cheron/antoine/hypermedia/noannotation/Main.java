package fr.cheron.antoine.hypermedia.noannotation;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.ipc.netty.http.server.HttpServer;

import fr.cheron.antoine.hypermedia.noannotation.rest.ProductApi;
import fr.cheron.antoine.hypermedia.noannotation.services.ProductService;

public class Main {

  public static void main(String[] args) {
    final ProductService productService = new ProductService();
    final ProductApi productApi = new ProductApi(productService);

    final RouterFunction<ServerResponse> routerFunction = RouterFunctions.
      nest(RequestPredicates.path("/products"), productApi.routerFunction);

    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
    final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.
      create(Config.HOST, Config.PORT).
      startAndAwait(adapter);
  }

}
