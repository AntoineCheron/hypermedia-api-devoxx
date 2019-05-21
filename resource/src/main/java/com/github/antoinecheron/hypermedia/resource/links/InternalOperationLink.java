package com.github.antoinecheron.hypermedia.resource.links;

import java.util.function.BiFunction;

import lombok.Getter;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.HypermediaControlBuilder;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public class InternalOperationLink<T, C> implements HypermediaControlBuilder<T> {

  @Getter private final String relation;
  private final Class<C> controllerClass;
  private final BiFunction<T, C, ?> methodCall;

  public InternalOperationLink(String relation, Class<C> controllerClass, BiFunction<T, C, ?> methodCall) {
    this.relation = relation;
    this.controllerClass = controllerClass;
    this.methodCall = methodCall;
  }

  @Override
  public Mono<Link> build(T resource) {
    return linkTo(this.methodCall.apply(resource, methodOn(controllerClass))).withRel(this.relation).toMono();
  }

}
