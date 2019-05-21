package com.github.antoinecheron.hypermedia.annotated.abstractions;

import reactor.core.publisher.Mono;

public interface FindByIdRepository<T> {

  Mono<T> findById(String id);

}
