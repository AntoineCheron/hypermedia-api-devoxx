package fr.cheron.antoine.hypermedia.noannotation.exceptions;

import reactor.core.publisher.Mono;

public class ForbiddenResourceOverrideException extends Exception {

  public static <T> Mono<T> asMono() {
    return Mono.error(new ForbiddenResourceOverrideException());
  }

}
