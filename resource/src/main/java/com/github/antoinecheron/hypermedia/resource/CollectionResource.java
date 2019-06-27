package com.github.antoinecheron.hypermedia.resource;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.links.HypermediaControlBuilder;

public class CollectionResource<T, TC extends Collection<T>> extends Resource<TC, CollectionModel<T>> {

  public CollectionResource(Class<TC> resourceType, Optional<HypermediaControlBuilder<TC>> maybeSelfLinkBuilder, Map<String, LinkHolder<TC>> selfOperationsResolver, Map<String, LinkHolder<TC>> internalLinksResolver, Map<String, LinkHolder<TC>> externalLinksResolver) {
    super(resourceType, maybeSelfLinkBuilder, selfOperationsResolver, internalLinksResolver, externalLinksResolver);
  }

  public Mono<CollectionModel<T>> representation(TC resourceState) {
    return this.resolveAllAvailableLinks(resourceState)
      .map(links -> new CollectionModel<>(resourceState, links));
  }

}
