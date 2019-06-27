package com.github.antoinecheron.hypermedia.resource;

import java.util.Map;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.links.HypermediaControlBuilder;

public class EntityResource<T> extends Resource<T, EntityModel<T>> {

  public EntityResource(Class<T> resourceType, Optional<HypermediaControlBuilder<T>> maybeSelfLinkBuilder, Map<String, LinkHolder<T>> selfOperationsResolver, Map<String, LinkHolder<T>> internalLinksResolver, Map<String, LinkHolder<T>> externalLinksResolver) {
    super(resourceType, maybeSelfLinkBuilder, selfOperationsResolver, internalLinksResolver, externalLinksResolver);
  }

  public Mono<EntityModel<T>> representation(T resourceState) {
    return this.resolveAllAvailableLinks(resourceState)
      .map(links -> new EntityModel<>(resourceState, links));
  }

}
