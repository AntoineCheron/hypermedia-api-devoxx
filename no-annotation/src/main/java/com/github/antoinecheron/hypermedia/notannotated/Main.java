package com.github.antoinecheron.hypermedia.notannotated;

import java.time.Duration;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import com.github.antoinecheron.hypermedia.notannotated.configuration.MongoConfiguration;
import com.github.antoinecheron.hypermedia.notannotated.process.*;
import com.github.antoinecheron.hypermedia.notannotated.product.ReactiveMongoProductRepository;
import com.github.antoinecheron.hypermedia.notannotated.product.ProductApi;
import com.github.antoinecheron.hypermedia.notannotated.user.*;

public class Main {

  public static void main(String[] args) {
    final var database = MongoConfiguration.getMongoOperations();

    final var productService = new ReactiveMongoProductRepository(database);
    final var processService = new ReactiveMongoProcessRepository(database);
    final var userService = new ReactiveMongoUserRepository(database);

    insertDataOnStartup(userService, processService);

    final var userApi = new UserConnexionApi(userService);

    final var routerFunction = RouterFunctions
      .nest(RequestPredicates.path("/products"), new ProductApi(productService).routerFunction)
      .andNest(RequestPredicates.path("/process"), new ProcessApi(processService).routerFunction)
      .andRoute(RequestPredicates.POST(UserConnexionApi.DEFAULT_LOGIN_PATH), userApi::login)
      .andRoute(RequestPredicates.POST(UserConnexionApi.DEFAULT_REGISTRATION_PATH), userApi::register);

    startHttpServer(routerFunction);
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

  private static void insertDataOnStartup(UserRepository userRepository, ProcessRepository processService) {
    final var firstManagerMono = userRepository.addUser(
      new UserCreationFormWithRole("Foo", "Bar", "foo@bar.io", "pass", "pass", UserRole.BANK_MANAGER)
    );

    firstManagerMono.flatMap(firstManager -> {
      final var firstClientMono = userRepository.addUser(
        new UserCreationFormWithRole("Client", "Bar", "client@bar.io", "pass2", "pass2", UserRole.CLIENT)
      );

      return Mono.zip(Mono.just(firstManager), firstClientMono);
    }).flatMap(tuple -> {
      final var firstManager = tuple.getT1();
      final var firstClient = tuple.getT2();

      return processService.createOne(new ProcessCreationForm("Housing loan", firstManager.getId(), firstClient.getId(), firstManager.getId(), firstManager.getId(), ProcessCategory.HOUSING_LOAN, "Description"));
    }).subscribeOn(Config.APPLICATION_SCHEDULER)
      .subscribe(ignoredProcess -> System.out.println("Initial data successfully pushed into the database"));
  }

}
