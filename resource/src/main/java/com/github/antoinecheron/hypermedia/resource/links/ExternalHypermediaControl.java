package com.github.antoinecheron.hypermedia.resource.links;

import java.util.Collections;
import java.util.function.Supplier;

import lombok.Getter;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.HypermediaControlBuilder;

public class ExternalHypermediaControl<T> implements HypermediaControlBuilder<T> {

  @Getter private final String relation;

  public ExternalHypermediaControl(String relation, String swaggerUrl, String operationId) {
    this.relation = relation;
  }

  private static Supplier<Object> resolveRemoteSwaggerOperation(String swaggerUrl, String operationId) {
    // TODO
    return Collections::emptyMap;
  }

  @Override
  public Mono<Link> build(Object resource) {
    return Mono.just(new Link("fake.href", "fake.rel"));
  }
}
