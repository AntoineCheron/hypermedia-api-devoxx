package com.github.antoinecheron.hypermedia.notannotated.process;

import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.notannotated.Config;

public class ReactiveMongoProcessRepository implements ProcessRepository {

  private final static String COLLECTION_NAME = "process";
  private final ReactiveMongoOperations mongoOperations;
  
  public ReactiveMongoProcessRepository(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Flux<ProcessSummary> list() {
    return mongoOperations.find(processSummaryQuery, ProcessSummary.class, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Process> findById(String id) {
    return mongoOperations.findOne(getIdQuery(id), Process.class, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Boolean> deleteOneById(String id) {
    return mongoOperations.findAndRemove(getIdQuery(id), Process.class, COLLECTION_NAME)
      .map((notUsed) -> true).onErrorReturn(false)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Process> createOne(ProcessCreationForm processWithoutId) {
    var process = processWithoutId.provideId(UUID.randomUUID().toString());
    return mongoOperations.insert(process, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<Process> updateOneById(Process process) {
    return mongoOperations.findAndReplace(getIdQuery(process.getId()), process, COLLECTION_NAME)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  private Query getIdQuery(String id) {
    return new Query(this.getIdCriteria(id));
  }

  private Criteria getIdCriteria(String id) {
    return Criteria.where("_id").is(id);
  }

  private static final Query processSummaryQuery = new Query();
  static {{
    processSummaryQuery.fields()
      .include("_id")
      .include("name")
      .include("client")
      .include("advisor")
      .include("category")
      .include("state");
  }}

}
