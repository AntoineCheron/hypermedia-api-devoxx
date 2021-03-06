package com.github.antoinecheron.hypermedia.notannotated.product;

import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.Config;

public class ReactiveMongoProductRepository implements ProductRepository {

  private final static String COLLECTION_NAME = "products";
  private final ReactiveMongoOperations mongoOperations;

  public ReactiveMongoProductRepository(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Flux<ProductSummary> list() {
    return mongoOperations.find(productSummaryQuery, ProductSummary.class, COLLECTION_NAME)
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
    var product = productWithoutId.provideId(UUID.randomUUID().toString());
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
