package com.github.antoinecheron.hypermedia.resource.links;

import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.HypermediaControlBuilder;

public class ExternalHyperlink<T> implements HypermediaControlBuilder<T> {

  private final Link link;

  public ExternalHyperlink(String name, String url) {
    this.link = new Link(url, name);
  }

  @Override
  public String getRelation() {
    return link.getRel().value();
  }

  @Override
  public Mono<Link> build(T resource) {
    return Mono.just(link);
  }

}
