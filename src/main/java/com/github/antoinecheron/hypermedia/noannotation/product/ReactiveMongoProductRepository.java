package com.github.antoinecheron.hypermedia.noannotation.product;

import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.noannotation.Config;

public class ReactiveMongoProductRepository implements ProductRepository {

  private final static String COLLECTION_NAME = "products";
  private final ReactiveMongoOperations mongoOperations;

  public ReactiveMongoProductRepository(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Flux<ProductSummary> list() {
    // TODO : find a way to have Mongo return a document that can be directly converted into a ProductSummary
    return mongoOperations.findAll(Product.class, COLLECTION_NAME)
      .map(ProductSummary::fromProduct)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> findById(String id) {
    return mongoOperations.findOne(getIdQuery(id), Product.class, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Boolean> deleteOneById(String id) {
    return mongoOperations.findAndRemove(getIdQuery(id), Product.class, COLLECTION_NAME)
      .map((notUsed) -> true).onErrorReturn(false)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> createOne(ProductWithoutId productWithoutId) {
    var product = productWithoutId.toProduct(UUID.randomUUID().toString());
    return mongoOperations.insert(product, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Product> updateOneById(Product product) {
    return mongoOperations.findAndReplace(getIdQuery(product.getId()), product, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Query getIdQuery(String id) {
    return new Query(this.getIdCriteria(id));
  }

  private Criteria getIdCriteria(String id) {
    return Criteria.where("_id").is(id);
  }

}
