package com.github.antoinecheron.hypermedia.resource;

import java.util.Optional;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.links.HypermediaControlBuilder;

@AllArgsConstructor
public class LinkHolder<T> {

  private final Predicate<T> isLinkAvailablePredicate;
  private final HypermediaControlBuilder<T> linkBuilder;

  public boolean isLinkAvailable(T resourceState) {
    return this.isLinkAvailablePredicate.test(resourceState);
  }

  public Optional<HypermediaControlBuilder<T>> getLinkBuilder(T resourceState) {
    return isLinkAvailable(resourceState)
      ? Optional.of(linkBuilder)
      : Optional.empty();
  }

  public Mono<Link> getLink(T resourceState) {
    return this.getLinkBuilder(resourceState)
      .map(link -> link.build(resourceState))
      .orElseGet(Mono::empty);
  }

}
