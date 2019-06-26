package com.github.antoinecheron.hypermedia.resource;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.antoinecheron.hypermedia.resource.links.ExternalHyperlink;
import com.github.antoinecheron.hypermedia.resource.links.ExternalHypermediaControl;
import com.github.antoinecheron.hypermedia.resource.links.InternalOperationLink;

public final class ResourceBuilder<T, C> {

  private final Class<T> resourceType;
  private final Class<C> controllerClass;
  private Optional<HypermediaControlBuilder<T>> selfLinkBuilder;

  private ResourceBuilder(Class<T> resourceType, Class<C> controllerClass) {
    this.resourceType = resourceType;
    this.controllerClass = controllerClass;
    this.selfLinkBuilder = Optional.empty();
  }

  public static <A, C> ResourceBuilder<A, C> of(Class<A> resourceType, Class<C> controllerClass) {
    return new ResourceBuilder<>(resourceType, controllerClass);
  }

  public ResourceBuilder<T, C> self(BiFunction<T, C, ?> methodCall) {
    this.selfLinkBuilder = Optional.of(new InternalOperationLink<>("self", controllerClass, methodCall));
    return this;
  }

  public ResourceBuilder<T, C>.Operations withOperations() {
    return new Operations(resourceType, controllerClass, this.selfLinkBuilder);
  }

  public ResourceBuilder<T, C>.InternalLinks withInternalLinks() {
    return new InternalLinks(resourceType, this.selfLinkBuilder, Collections.emptyMap());
  }

  public ResourceBuilder<T, C>.ExternalLinks withExternalLinks() {
    return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.emptyMap(), Collections.emptyMap());
  }

  public Resource<T> build() {
    return withExternalLinks().build();
  }

  public final class Operations {

    private final Class<T> resourceType;
    private final Class<C> controllerClass;
    private Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
    private final Map<String, LinkHolder<T>> selfOperationsResolver;

    Operations(Class<T> resourceType, Class<C> controllerClass, Optional<HypermediaControlBuilder<T>> selfLinkBuilder) {
      this.resourceType = resourceType;
      this.controllerClass = controllerClass;
      this.selfLinkBuilder = selfLinkBuilder;
      this.selfOperationsResolver = new HashMap<>();
    }

    public Operations availableIf(String relation, Predicate<T> predicate, BiFunction<T, C, ?> methodCall) {
      this.selfOperationsResolver.put(relation,
        new LinkHolder<>(
          predicate,
          new InternalOperationLink<>(relation, controllerClass, methodCall)
        )
      );
      return this;
    }

    public Operations alwaysAvailable(String name, BiFunction<T, C, ?> methodCall) {
      return availableIf(name, ALWAYS_AVAILABLE, methodCall);
    }

    public InternalLinks withInternalLinks() {
      return new InternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver));
    }

    public ExternalLinks withExternalLinks() {
      return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver), Collections.emptyMap());
    }

    public Resource<T> build() {
      return withExternalLinks().build();
    }

  }

  public final class InternalLinks {

    private final Class<T> resourceType;
    private Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
    private final Map<String, LinkHolder<T>> selfOperationsResolver;
    private final Map<String, LinkHolder<T>> internalLinksResolver;

    public InternalLinks(Class<T> resourceType, Optional<HypermediaControlBuilder<T>> selfLinkBuilder, Map<String, LinkHolder<T>> selfOperationsResolver) {
      this.resourceType = resourceType;
      this.selfLinkBuilder = selfLinkBuilder;
      this.selfOperationsResolver = selfOperationsResolver;
      this.internalLinksResolver = new HashMap<>();
    }

    public <C> InternalLinks availableIf(String relation, Predicate<T> predicate, Class<C> controllerClass, BiFunction<T, C, ?> methodCall) {
      this.selfOperationsResolver.put(relation,
        new LinkHolder<>(
          predicate,
          new InternalOperationLink<>(relation, controllerClass, methodCall)
        )
      );
      return this;
    }

    public <C> InternalLinks alwaysAvailable(String name, Class<C> controllerClass, BiFunction<T, C, ?> methodCall) {
      return availableIf(name, ALWAYS_AVAILABLE, controllerClass, methodCall);
    }

    public ExternalLinks withExternalLinks() {
      return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver), Collections.unmodifiableMap(internalLinksResolver));
    }

    public Resource<T> build() {
      return withExternalLinks().build();
    }

  }

  public final class ExternalLinks {

    private final Class<T> resourceType;
    private Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
    private final Map<String, LinkHolder<T>> selfOperationsResolver;
    private final Map<String, LinkHolder<T>> internalLinksResolver;
    private final Map<String, LinkHolder<T>> externalLinksResolver;

    public ExternalLinks(Class<T> resourceType, Optional<HypermediaControlBuilder<T>> selfLinkBuilder, Map<String, LinkHolder<T>> selfOperationsResolver, Map<String, LinkHolder<T>> internalLinksResolver) {
      this.resourceType = resourceType;
      this.selfLinkBuilder = selfLinkBuilder;
      this.selfOperationsResolver = selfOperationsResolver;
      this.internalLinksResolver = internalLinksResolver;
      this.externalLinksResolver = new HashMap<>();
    }

    public ExternalLinks toUrl(String relationName, String url, Predicate<T> predicate) {
      this.externalLinksResolver.put(
        relationName,
        new LinkHolder<>(predicate, new ExternalHyperlink<>(relationName, url))
      );
      return this;
    }

    public ExternalLinks toUrl(String relationName, Function<T, String> urlBuilder, Predicate<T> predicate) {
      this.externalLinksResolver.put(
        relationName,
        new LinkHolder<>(predicate, new ExternalHyperlink<>(relationName, urlBuilder))
      );
      return this;
    }

    public ExternalLinks toUrlAlwaysAvailable(String relationName, String url) {
      return toUrl(relationName, url, ALWAYS_AVAILABLE);
    }

    public ExternalLinks toUrlAlwaysAvailable(String relationName, Function<T, String> urlBuilder) {
      return toUrl(relationName, urlBuilder, ALWAYS_AVAILABLE);
    }

    public ExternalLinks toOperation(String relationName, String swaggerUrl, String operationId, Predicate<T> predicate) {
      this.externalLinksResolver.put(
        relationName,
        new LinkHolder<>(predicate, new ExternalHypermediaControl<>(relationName, swaggerUrl, operationId))
      );
      return this;
    }

    public ExternalLinks toOperationAlwaysAvailable(String relationName, String swaggerUrl, String operationId) {
      return toOperation(relationName, swaggerUrl, operationId, ALWAYS_AVAILABLE);
    }

    public Resource<T> build() {
      return new Resource<>(resourceType, selfLinkBuilder, selfOperationsResolver, internalLinksResolver, externalLinksResolver);
    }

  }

  private final Predicate<T> ALWAYS_AVAILABLE = ignored -> true;

}
