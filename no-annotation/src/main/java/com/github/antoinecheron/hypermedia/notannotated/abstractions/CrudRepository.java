package com.github.antoinecheron.hypermedia.notannotated.abstractions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudRepository<T, CREATION_FORM extends CreationFormOf<T>, SUMMARY> {

  Flux<SUMMARY> list();

  Mono<T> findById(String id);

  Mono<Boolean> deleteOneById(String id);

  Mono<T> createOne(CREATION_FORM entityWithoutId);

  Mono<T> updateOneById(T entity);

}
