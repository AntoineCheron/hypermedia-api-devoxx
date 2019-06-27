package com.github.antoinecheron.hypermedia.resource.builder;

import java.util.*;
import java.util.function.Predicate;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.github.antoinecheron.hypermedia.resource.LinkHolder;
import com.github.antoinecheron.hypermedia.resource.Resource;
import com.github.antoinecheron.hypermedia.resource.links.ExternalHyperlink;
import com.github.antoinecheron.hypermedia.resource.links.ExternalHypermediaControl;
import com.github.antoinecheron.hypermedia.resource.links.HypermediaControlBuilder;
import com.github.antoinecheron.hypermedia.resource.links.InternalOperationLink;
import com.github.antoinecheron.hypermedia.resource.providers.LinkProvider;
import com.github.antoinecheron.hypermedia.resource.providers.OperationProvider;

public abstract class ResourceBuilder<T, C, RM extends RepresentationModel<RM>> {

  protected final Class<T> resourceType;
  protected final Class<C> controllerClass;
  protected Optional<HypermediaControlBuilder<T>> selfLinkBuilder;

  protected ResourceBuilder(Class<T> resourceType, Class<C> controllerClass) {
    this.resourceType = resourceType;
    this.controllerClass = controllerClass;
    this.selfLinkBuilder = Optional.empty();
  }

  public static <A, C> ResourceBuilder<A, C, EntityModel<A>> ofEntity(Class<A> resourceType, Class<C> controllerClass) {
    return EntityResourceBuilder.of(resourceType, controllerClass);
  }

  public static <A, CA extends Collection<A>, C> ResourceBuilder<CA, C, CollectionModel<A>> ofCollection(Class<CA> resourceType, Class<C> controllerClass) {
    return CollectionResourceBuilder.of(resourceType, controllerClass);
  }

  public ResourceBuilder<T, C, RM> withSelfLink(OperationProvider<T, C> methodCall) {
    this.selfLinkBuilder = Optional.of(new InternalOperationLink<>("self", controllerClass, methodCall));
    return this;
  }

  public ResourceBuilder<T, C, RM>.Operations withOperations() {
    return new Operations(resourceType, controllerClass, this.selfLinkBuilder);
  }

  public ResourceBuilder<T, C, RM>.InternalLinks withInternalLinks() {
    return new InternalLinks(resourceType, this.selfLinkBuilder, Collections.emptyMap());
  }

  public ResourceBuilder<T, C, RM>.ExternalLinks withExternalLinks() {
    return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.emptyMap(), Collections.emptyMap());
  }

  public Resource<T, RM> build() {
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

    public Operations operation(String relation, Predicate<T> predicate, OperationProvider<T, C> methodCall) {
      this.selfOperationsResolver.put(relation,
        new LinkHolder<>(
          predicate,
          new InternalOperationLink<>(relation, controllerClass, methodCall)
        )
      );
      return this;
    }

    public Operations operation(String name, OperationProvider<T, C> methodCall) {
      return operation(name, ALWAYS_AVAILABLE, methodCall);
    }

    public InternalLinks withInternalLinks() {
      return new InternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver));
    }

    public ExternalLinks withExternalLinks() {
      return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver), Collections.emptyMap());
    }

    public Resource<T, RM> build() {
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

    public <C> InternalLinks availableIf(String relation, Predicate<T> predicate, Class<C> controllerClass, OperationProvider<T, C> methodCall) {
      this.selfOperationsResolver.put(relation,
        new LinkHolder<>(
          predicate,
          new InternalOperationLink<>(relation, controllerClass, methodCall)
        )
      );
      return this;
    }

    public <C> InternalLinks alwaysAvailable(String name, Class<C> controllerClass, OperationProvider<T, C> methodCall) {
      return availableIf(name, ALWAYS_AVAILABLE, controllerClass, methodCall);
    }

    public ExternalLinks withExternalLinks() {
      return new ExternalLinks(resourceType, this.selfLinkBuilder, Collections.unmodifiableMap(selfOperationsResolver), Collections.unmodifiableMap(internalLinksResolver));
    }

    public Resource<T, RM> build() {
      return withExternalLinks().build();
    }

  }

  public final class ExternalLinks {

    protected final Class<T> resourceType;
    protected Optional<HypermediaControlBuilder<T>> selfLinkBuilder;
    protected final Map<String, LinkHolder<T>> selfOperationsResolver;
    protected final Map<String, LinkHolder<T>> internalLinksResolver;
    protected final Map<String, LinkHolder<T>> externalLinksResolver;

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

    public ExternalLinks toUrl(String relationName, LinkProvider<T> urlBuilder, Predicate<T> predicate) {
      this.externalLinksResolver.put(
        relationName,
        new LinkHolder<>(predicate, new ExternalHyperlink<>(relationName, urlBuilder))
      );
      return this;
    }

    public ExternalLinks toUrlAlwaysAvailable(String relationName, String url) {
      return toUrl(relationName, url, ALWAYS_AVAILABLE);
    }

    public ExternalLinks toUrlAlwaysAvailable(String relationName, LinkProvider<T> urlBuilder) {
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

    public Resource<T, RM> build() {
      return ResourceBuilder.this.build(this);
    }

  }

  protected abstract Resource<T, RM> build(ExternalLinks externalLinksBuilder);

  protected final Predicate<T> ALWAYS_AVAILABLE = ignored -> true;

}