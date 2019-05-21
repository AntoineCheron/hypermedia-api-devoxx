package com.github.antoinecheron.hypermedia.resource;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Resource<T> {

  @Getter private final Class<T> resourceType;
  private Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
  @Getter private final Map<String, LinkHolder<T>> selfOperationsResolver;
  @Getter private final Map<String, LinkHolder<T>> internalLinksResolver;
  @Getter private final Map<String, LinkHolder<T>> externalLinksResolver;
  @Getter private final Map<String, LinkHolder<T>> availableLinksResolver;

  Resource(Class<T> resourceType, Optional<HypermediaControlBuilder<T>> maybeSelfLinkBuilder, Map<String, LinkHolder<T>> selfOperationsResolver, Map<String, LinkHolder<T>> internalLinksResolver, Map<String, LinkHolder<T>> externalLinksResolver) {
    this.resourceType = resourceType;
    this.selfLinkBuilder = maybeSelfLinkBuilder;
    this.selfOperationsResolver = selfOperationsResolver;
    this.internalLinksResolver = internalLinksResolver;
    this.externalLinksResolver = externalLinksResolver;

    final Map<String, LinkHolder<T>> availableLinksResolver = new HashMap<>();
    maybeSelfLinkBuilder.ifPresent(selfLinkBuilder ->
      availableLinksResolver.put("self", new LinkHolder<>(ignored -> true, selfLinkBuilder))
    );
    availableLinksResolver.putAll(selfOperationsResolver);
    availableLinksResolver.putAll(internalLinksResolver);
    availableLinksResolver.putAll(externalLinksResolver);
    this.availableLinksResolver = availableLinksResolver;
  }

  public Mono<EntityModel<T>> entityRepresentation(T resourceState) {
    // TODO: support CollectionModel and PagedModel
    return this.resolveAllAvailableLinks(resourceState)
      .map(links -> new EntityModel<>(resourceState, links));
  }

  public boolean isOperationAvailable(String operationId, T resourceWithState) {
    return Optional.of(this.selfOperationsResolver.get(operationId))
      .map(linkHolder -> linkHolder.isLinkAvailable(resourceWithState))
      .orElse(true);
  }

  public Mono<Link> resolveSelfLink(T resourceState) {
    return this.selfLinkBuilder.map(builder -> builder.build(resourceState)).orElse(Mono.empty());
  }

  public Mono<List<Link>> resolveAvailableOperations(T resourceState) {
    return this.resolveAvailableLinks(this.selfOperationsResolver, resourceState);
  }

  public Mono<List<Link>> resolveAvailableInternalLinks(T resourceState) {
    return this.resolveAvailableLinks(this.internalLinksResolver, resourceState);
  }

  public Mono<List<Link>> resolveAvailableExternalLinks(T resourceState) {
    return this.resolveAvailableLinks(this.externalLinksResolver, resourceState);
  }

  public Mono<List<Link>> resolveAllAvailableLinks(T resourceState) {
    return this.resolveAvailableLinks(this.availableLinksResolver, resourceState);
  }

  private Mono<List<Link>> resolveAvailableLinks(Map<String, LinkHolder<T>> input, T resourceState) {
    final List<Mono<Link>> links = input
      .values()
      .stream()
      .map(linkBuilder -> linkBuilder.getLink(resourceState))
      .collect(Collectors.toList());

    return Flux.merge(links).collectList();
  }

  public boolean isResourceOf(Object o) {
    // TODO : check all subclasses except Object (unit test)
    return this.resourceType.isAssignableFrom(o.getClass());
  }

  public static <A, C> ResourceBuilder<A, C> builder(Class<A> resourceClass, Class<C> controllerClass) {
    return ResourceBuilder.of(resourceClass, controllerClass);
  }

}
