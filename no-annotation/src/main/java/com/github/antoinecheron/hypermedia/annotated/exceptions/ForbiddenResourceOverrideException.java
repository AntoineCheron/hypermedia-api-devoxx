package com.github.antoinecheron.hypermedia.annotated.exceptions;

import reactor.core.publisher.Mono;

public class ForbiddenResourceOverrideException extends Exception {

  public static <T> Mono<T> asMono() {
    return Mono.error(new ForbiddenResourceOverrideException());
  }

}
