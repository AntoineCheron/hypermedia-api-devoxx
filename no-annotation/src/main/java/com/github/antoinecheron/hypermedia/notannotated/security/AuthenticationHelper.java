package com.github.antoinecheron.hypermedia.notannotated.security;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.user.User;
import com.github.antoinecheron.hypermedia.notannotated.utils.Responses;

public class AuthenticationHelper {

  public static final String AUTHORIZATION_HEADER_KEY = "Authorization";

  public static Mono<ServerResponse> withAuthenticationOptional(ServerRequest request, Function<Optional<User>, Mono<ServerResponse>> requestHandler) {
    return requestHandler.apply(retrieveUserFromRequest(request));
  }

  public static Mono<ServerResponse> withAuthentication(ServerRequest request, Function<User, Mono<ServerResponse>> requestHandler) {
    return retrieveUserFromRequest(request)
      .map(requestHandler)
      .orElseGet(Responses::unauthorized);
  }

  private static Optional<User> retrieveUserFromRequest(ServerRequest request) {
    return Optional.ofNullable(request.headers().header(AUTHORIZATION_HEADER_KEY).get(0))
      .flatMap(JsonWebTokenService::verifyToken);
  }

}
