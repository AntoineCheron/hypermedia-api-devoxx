package com.github.antoinecheron.hypermedia.resource.builder;

import java.util.Collection;

import org.springframework.hateoas.CollectionModel;

import com.github.antoinecheron.hypermedia.resource.CollectionResource;
import com.github.antoinecheron.hypermedia.resource.Resource;

public final class CollectionResourceBuilder<T, TC extends Collection<T>, C> extends ResourceBuilder<TC, C, CollectionModel<T>> {

  private CollectionResourceBuilder(Class<TC> resourceType, Class<C> controllerClass) {
    super(resourceType, controllerClass);
  }

  public static <A, CA extends Collection<A>, C> CollectionResourceBuilder<A, CA, C> of(Class<CA> resourceType, Class<C> controllerClass) {
    return new CollectionResourceBuilder<>(resourceType, controllerClass);
  }

  protected Resource<TC, CollectionModel<T>> build(ExternalLinks externalLinksBuilder) {
    return new CollectionResource<>(
      externalLinksBuilder.resourceType,
      externalLinksBuilder.selfLinkBuilder,
      externalLinksBuilder.selfOperationsResolver,
      externalLinksBuilder.internalLinksResolver,
      externalLinksBuilder.externalLinksResolver
    );
  }

}
