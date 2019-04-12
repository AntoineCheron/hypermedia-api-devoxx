package com.github.antoinecheron.hypermedia.noannotation.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

  Flux<ProductSummary> list();

  Mono<Product> findById(String id);

  Mono<Boolean> deleteOneById(String id);

  Mono<Product> createOne(ProductWithoutId productWithoutId);

  Mono<Product> updateOneById(Product product);

}
