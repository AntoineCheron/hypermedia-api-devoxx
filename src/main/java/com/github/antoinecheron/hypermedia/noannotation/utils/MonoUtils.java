package com.github.antoinecheron.hypermedia.noannotation.utils;

import java.util.Optional;
import java.util.function.Supplier;

import reactor.core.publisher.Mono;

public class MonoUtils {

  public static <T> Mono<T> fromOptional(Optional<T> option, Supplier<? extends Exception> errorSupplier) {
    return option.map(Mono::just).orElseGet(() -> Mono.error(errorSupplier.get()));
  }

}
