package com.github.antoinecheron.hypermedia.resource;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.builder.ResourceBuilder;
import com.github.antoinecheron.hypermedia.resource.links.HypermediaControlBuilder;

public abstract class Resource<T, RM extends RepresentationModel<RM>> {

  @Getter protected final Class<T> resourceType;
  protected Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
  @Getter protected final Map<String, LinkHolder<T>> selfOperationsResolver;
  @Getter protected final Map<String, LinkHolder<T>> internalLinksResolver;
  @Getter protected final Map<String, LinkHolder<T>> externalLinksResolver;
  @Getter protected final Map<String, LinkHolder<T>> availableLinksResolver;

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

  public abstract Mono<RM> representation(T resourceState);

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

  protected Mono<List<Link>> resolveAvailableLinks(Map<String, LinkHolder<T>> input, T resourceState) {
    final List<Mono<Link>> links = input
      .values()
      .stream()
      .map(linkBuilder -> linkBuilder.getLink(resourceState))
      .collect(Collectors.toList());

    return Flux.merge(links).collectList();
  }

  public static <A, C> ResourceBuilder<A, C, EntityModel<A>> entityBuilder(Class<A> resourceClass, Class<C> controllerClass) {
    return ResourceBuilder.ofEntity(resourceClass, controllerClass);
  }

  public static <A, CA extends Collection<A>, C> ResourceBuilder<CA, C, CollectionModel<A>> collectionBuilder(Class<CA> collectionClass, Class<C> controllerClass) {
    return ResourceBuilder.ofCollection(collectionClass, controllerClass);
  }

}
