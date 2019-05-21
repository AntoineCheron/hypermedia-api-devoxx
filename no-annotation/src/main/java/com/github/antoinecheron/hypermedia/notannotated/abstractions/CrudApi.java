package com.github.antoinecheron.hypermedia.notannotated.abstractions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.Config;
import com.github.antoinecheron.hypermedia.notannotated.exceptions.ForbiddenResourceOverrideException;
import com.github.antoinecheron.hypermedia.notannotated.json.JsonWriter;
import com.github.antoinecheron.hypermedia.notannotated.utils.Responses;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

public abstract class CrudApi<TYPE, TYPE_WITHOUT_ID extends CreationFormOf<TYPE>, TYPE_SUMMARY, REPOSITORY extends CrudRepository<TYPE, TYPE_WITHOUT_ID, TYPE_SUMMARY>> {

  // Path variables
  public static final String ID_PATH_VARIABLE = "id";

  // Paths
  public static final String ONE_ELEMENT =  "/{" + ID_PATH_VARIABLE + "}";

  private final Class<TYPE> typeClass;
  private final Class<TYPE_WITHOUT_ID> typeWithoutIdClass;
  private final Class<TYPE_SUMMARY> typeSummaryClass;
  private final REPOSITORY service;

  public final RouterFunction<ServerResponse> routerFunction;

  public CrudApi(REPOSITORY service, Class<TYPE> typeClass, Class<TYPE_WITHOUT_ID> typeWithoutIdClass, Class<TYPE_SUMMARY> typeSummaryClass) {
    this.typeClass = typeClass;
    this.typeWithoutIdClass = typeWithoutIdClass;
    this.typeSummaryClass = typeSummaryClass;
    this.service = service;

    final var routerFunctionWithCrudOperations = RouterFunctions
      .route(GET(""), this::getAll)
      .andRoute(POST("").and(contentType(MediaType.APPLICATION_JSON)), this::createOne)
      .andRoute(GET(ONE_ELEMENT), this::getOneById)
      .andRoute(PUT(ONE_ELEMENT).and(contentType(MediaType.APPLICATION_JSON)), this::updateOneById)
      .andRoute(DELETE(ONE_ELEMENT), this::deleteOneById);
    this.routerFunction = extendRouterFunction(routerFunctionWithCrudOperations);
  }

  /**
   * Use this function to extend the features of this default CRUD API
   * @param routerFunction
   * @return
   */
  protected RouterFunction<ServerResponse> extendRouterFunction(RouterFunction<ServerResponse> routerFunction) {
    return routerFunction;
  }

  protected Mono<ServerResponse> getAll(ServerRequest request) {
    return this.service.
      list().
      collectList().
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      onErrorResume(JsonProcessingException.class, Responses::internalServerError).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  protected Mono<ServerResponse> getOneById(ServerRequest request) {
    return this.service.
      findById(request.pathVariable(ID_PATH_VARIABLE)).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      switchIfEmpty(Responses.notFound()).
      onErrorResume(JsonProcessingException.class,Responses::internalServerError).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  protected Mono<ServerResponse> updateOneById(ServerRequest request) {
    return request.bodyToMono(this.typeWithoutIdClass).
      map((elWithoutId) -> elWithoutId.provideId(request.pathVariable("id"))).
      flatMap(this.service::updateOneById).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      switchIfEmpty(Responses.notFound()).
      onErrorResume(ServerWebInputException.class, Responses::badRequest).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  protected Mono<ServerResponse> deleteOneById(ServerRequest request) {
    return this.service.
      deleteOneById(request.pathVariable(ID_PATH_VARIABLE)).
      flatMap((success) -> success ? Responses.noContent() : Responses.internalServerError()).
      switchIfEmpty(Responses.notFound()).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  protected Mono<ServerResponse> createOne(ServerRequest request) {
    return request.bodyToMono(typeWithoutIdClass).
      flatMap(this.service::createOne).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      onErrorResume(ServerWebInputException.class, Responses::badRequest).
      onErrorResume(ForbiddenResourceOverrideException.class, Responses::forbidden).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}
