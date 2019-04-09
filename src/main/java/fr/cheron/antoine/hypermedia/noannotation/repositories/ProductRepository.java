package fr.cheron.antoine.hypermedia.noannotation.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import fr.cheron.antoine.hypermedia.noannotation.domain.Product;

public interface ProductRepository {

  Flux<Product> list();

  Mono<Product> findById(String id);

  Mono<Boolean> deleteOneById(String id);

  Mono<Boolean> createOne(Product product);

  Mono<Boolean> updateOneById(Product product);

}
