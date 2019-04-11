package com.github.antoinecheron.hypermedia.noannotation.product;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.noannotation.Config;
import com.github.antoinecheron.hypermedia.noannotation.exceptions.ForbiddenResourceOverrideException;
import com.github.antoinecheron.hypermedia.noannotation.exceptions.InvalidRequestBodyException;
import com.github.antoinecheron.hypermedia.noannotation.json.JsonWriter;
import com.github.antoinecheron.hypermedia.noannotation.utils.Responses;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

public class ProductApi {

  // Path variables
  public static final String PRODUCT_ID_PATH_VARIABLE = "id";

  // Paths
  public static final String BASE_PATH = "";
  public static final String ONE_PRODUCT = BASE_PATH + "/{" + PRODUCT_ID_PATH_VARIABLE + "}";

  private final ProductRepository productService;

  public final RouterFunction<ServerResponse> routerFunction;

  public ProductApi(ProductRepository productService) {
    this.productService = productService;

    this.routerFunction = RouterFunctions
      .route(GET(BASE_PATH), this::getAllProducts)
      .andRoute(POST(BASE_PATH).and(contentType(MediaType.APPLICATION_JSON)), this::createOneProduct)
      .andRoute(GET(ONE_PRODUCT), this::getOneProductById)
      .andRoute(PUT(ONE_PRODUCT).and(contentType(MediaType.APPLICATION_JSON)), this::updateOneProductById)
      .andRoute(DELETE(ONE_PRODUCT), this::deleteOneProductById);
  }

  private Mono<ServerResponse> getAllProducts(ServerRequest request) {
    return this.productService.
      list().
      collectList().
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      onErrorResume(JsonProcessingException.class, Responses::internalServerError).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> getOneProductById(ServerRequest request) {
    return this.productService.
      findById(request.pathVariable(PRODUCT_ID_PATH_VARIABLE)).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      switchIfEmpty(Responses.notFound()).
      onErrorResume(JsonProcessingException.class,Responses::internalServerError).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> updateOneProductById(ServerRequest request) {
    return request.bodyToMono(ProductWithoutId.class).
      map((productWithoutId) -> productWithoutId.toProduct(request.pathVariable("id"))).
      flatMap(this.productService::updateOneById).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      switchIfEmpty(Responses.notFound()).
      onErrorResume(InvalidRequestBodyException.class, Responses::badRequest).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> deleteOneProductById(ServerRequest request) {
    return this.productService.
      deleteOneById(request.pathVariable(PRODUCT_ID_PATH_VARIABLE)).
      flatMap((success) -> success ? Responses.noContent() : Responses.internalServerError()).
      switchIfEmpty(Responses.notFound()).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> createOneProduct(ServerRequest request) {
    return request.bodyToMono(ProductWithoutId.class).
      flatMap(this.productService::createOne).
      flatMap(JsonWriter::write).
      flatMap(Responses::ok).
      onErrorResume(ForbiddenResourceOverrideException.class, Responses::forbidden).
      onErrorResume(InvalidRequestBodyException.class, Responses::badRequest).
      subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}
