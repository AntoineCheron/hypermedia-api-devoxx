package com.github.antoinecheron.hypermedia.resource.links;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.Getter;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.HypermediaControlBuilder;

public class ExternalHypermediaControl<T> implements HypermediaControlBuilder<T> {

  @Getter private final String relation;
  private final Function<T, String> swaggerUrlBuilder;
  private final String operationId;
  private String result;

  public ExternalHypermediaControl(String relation, String swaggerUrl, String operationId) {
    this(relation, unused -> swaggerUrl, operationId);
  }

  public ExternalHypermediaControl(String relation, Function<T, String> swaggerUrlBuilder, String operationId) {
    this.relation = relation;
    this.swaggerUrlBuilder = swaggerUrlBuilder;
    this.operationId = operationId;
  }

  private static Supplier<Object> resolveRemoteSwaggerOperation(String swaggerUrl, String operationId) {
    // TODO
    return Collections::emptyMap;
  }

  @Override
  public Mono<Link> build(T resource) {
    return Mono.just(new Link("fake.href", "fake.rel"));
  }
}
