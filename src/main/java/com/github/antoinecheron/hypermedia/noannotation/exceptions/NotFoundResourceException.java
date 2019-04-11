package com.github.antoinecheron.hypermedia.noannotation.exceptions;

import reactor.core.publisher.Mono;

public class NotFoundResourceException extends Exception {

  public NotFoundResourceException(String resourceName) {
    super("Resource " + resourceName + " not found.");
  }

  public static <T> Mono<T> asMono(String resourceName) {
    return Mono.error(new NotFoundResourceException(resourceName));
  }

}
