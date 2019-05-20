package com.github.antoinecheron.hypermedia.notannotated.utils;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.security.AuthenticationHelper;

public class Responses {

  public static Mono<ServerResponse> ok(String result) {
    return ServerResponse.ok().body(Mono.just(result), String.class);
  }

  public static Mono<ServerResponse> created(URI location, String result) {
    return ServerResponse.created(location).body(Mono.just(result), String.class);
  }

  public static Mono<ServerResponse> badRequest(Exception e) {
    return fromException(HttpStatus.BAD_REQUEST, e);
  }

  public static Mono<ServerResponse> unauthorized(Exception... notUsedException) {
    return ServerResponse.status(HttpStatus.UNAUTHORIZED).syncBody("Please login first and then retry this request. You can login at /login.");
  }

  public static Mono<ServerResponse> forbidden(Exception... notUsedException) {
    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
  }

  public static Mono<ServerResponse> noContent(Object... notUsed) {
    return ServerResponse.noContent().build();
  }

  public static Mono<ServerResponse> loginOk(String token) {
    return ServerResponse.noContent().header(AuthenticationHelper.AUTHORIZATION_HEADER_KEY, token).build();
  }

  public static Mono<ServerResponse> notFound(Exception... notUsed) {
    return ServerResponse.notFound().build();
  }

  public static Mono<ServerResponse> internalServerError() {
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  public static Mono<ServerResponse> internalServerError(Exception e) {
    return fromException(HttpStatus.INTERNAL_SERVER_ERROR, e);
  }

  private static Mono<ServerResponse> fromException(HttpStatus status, Exception e) {
    final ServerResponse.BodyBuilder responseBuilder = ServerResponse.status(status);
    return e.getMessage() == null
      ? responseBuilder.build()
      : responseBuilder.body(Mono.just(e.getMessage()), String.class);
  }

}
