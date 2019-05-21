package com.github.antoinecheron.hypermedia.annotated.product;

import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.annotated.configuration.Config;
import com.github.antoinecheron.hypermedia.annotated.exceptions.InternalServerError;
import com.github.antoinecheron.hypermedia.annotated.exceptions.NotFoundResourceException;

@Repository
public class ReactiveMongoProductRepository implements ProductRepository {

  private final ReactiveMongoOperations mongoOperations;

  public ReactiveMongoProductRepository(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Flux<ProductSummary> list() {
    return mongoOperations.find(productSummaryQuery, ProductSummary.class)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> findById(String id) {
    return mongoOperations.findOne(getIdQuery(id), Product.class)
      .switchIfEmpty(Mono.error(NotFoundResourceException::new))
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Void> deleteOneById(String id) {
    return mongoOperations.findAndRemove(getIdQuery(id), Product.class)
      .then()
      .onErrorMap(e -> new InternalServerError())
      .switchIfEmpty(Mono.error(NotFoundResourceException::new))
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> createOne(ProductWithoutId productWithoutId) {
    var product = productWithoutId.provideId(UUID.randomUUID().toString());
    return mongoOperations.insert(product)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> updateOneById(Product product) {
    return mongoOperations.findAndReplace(getIdQuery(product.getId()), product)
      .thenReturn(product)
      .switchIfEmpty(Mono.error(NotFoundResourceException::new))
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Query getIdQuery(String id) {
    return new Query(this.getIdCriteria(id));
  }

  private Criteria getIdCriteria(String id) {
    return Criteria.where("_id").is(id);
  }

  private static final Query productSummaryQuery = new Query();
  static {{
      productSummaryQuery.fields()
        .include("_id")
        .include("title")
        .include("price")
        .include("inStock")
        .slice("photos", 1);
  }}

}
