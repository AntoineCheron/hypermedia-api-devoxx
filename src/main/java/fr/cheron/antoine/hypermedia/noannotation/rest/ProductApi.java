package fr.cheron.antoine.hypermedia.noannotation.rest;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import fr.cheron.antoine.hypermedia.noannotation.Config;
import fr.cheron.antoine.hypermedia.noannotation.domain.Product;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.ForbiddenResourceOverrideException;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.InvalidRequestBodyException;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.NotFoundResourceException;
import fr.cheron.antoine.hypermedia.noannotation.json.JsonWriter;
import fr.cheron.antoine.hypermedia.noannotation.json.reader.ProductReader;
import fr.cheron.antoine.hypermedia.noannotation.repositories.ProductRepository;
import fr.cheron.antoine.hypermedia.noannotation.utils.MonoUtils;
import fr.cheron.antoine.hypermedia.noannotation.utils.Responses;

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

    this.routerFunction = RouterFunctions.
        route(GET(BASE_PATH), this::getAllProducts).
        andRoute(POST(BASE_PATH).and(contentType(MediaType.APPLICATION_JSON)), this::createOneProduct).
        andRoute(GET(ONE_PRODUCT), this::getOneProductById).
        andRoute(PUT(ONE_PRODUCT).and(contentType(MediaType.APPLICATION_JSON)), this::updateOneProductById).
        andRoute(DELETE(ONE_PRODUCT), this::deleteOneProductById);
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
        onErrorResume(NotFoundResourceException.class, Responses::notFound).
        onErrorResume(JsonProcessingException.class,Responses::internalServerError).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> updateOneProductById(ServerRequest request) {
    return request.bodyToMono(String.class).
        flatMap(this::readProductFromRequestBody).
        flatMap(this.productService::updateOneById).
        flatMap((success) -> success ? Responses.noContent() : Responses.internalServerError()).
        onErrorResume(NotFoundResourceException.class, Responses::notFound).
        onErrorResume(InvalidRequestBodyException.class, Responses::badRequest).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> deleteOneProductById(ServerRequest request) {
    return this.productService.
        deleteOneById(request.pathVariable(PRODUCT_ID_PATH_VARIABLE)).
        flatMap((success) -> success ? Responses.noContent() : Responses.internalServerError()).
        onErrorResume(NotFoundResourceException.class, Responses::notFound).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<ServerResponse> createOneProduct(ServerRequest request) {
    return request.bodyToMono(String.class).
        flatMap(this::readProductFromRequestBody).
        flatMap(this.productService::createOne).
        flatMap((success) -> success ? Responses.noContent() : Responses.internalServerError()).
        onErrorResume(NotFoundResourceException.class, Responses::notFound).
        onErrorResume(ForbiddenResourceOverrideException.class, Responses::forbidden).
        onErrorResume(InvalidRequestBodyException.class, Responses::badRequest).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Mono<Product> readProductFromRequestBody(String body) {
    return MonoUtils.fromOptional(
        ProductReader.read(body),
        () -> new InvalidRequestBodyException(Product.class)
    );
  }

}
