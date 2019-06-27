package com.github.antoinecheron.hypermedia.resource.links;

import java.util.function.Function;

import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

public class ExternalHyperlink<T> implements HypermediaControlBuilder<T> {

  private final String name;
  private final Function<T, String> linkBuilder;

  public ExternalHyperlink(String name, String url) {
    this(name, unused -> url);
  }

  public ExternalHyperlink(String name, Function<T, String> linkBuilder) {
    this.name = name;
    this.linkBuilder = linkBuilder;
  }

  @Override
  public String getRelation() {
    return name;
  }

  @Override
  public Mono<Link> build(T resource) {
    return Mono.fromCallable(() -> new Link(this.name, this.linkBuilder.apply(resource)));
  }

}
