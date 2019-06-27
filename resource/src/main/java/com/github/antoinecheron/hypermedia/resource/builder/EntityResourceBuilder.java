package com.github.antoinecheron.hypermedia.resource.builder;

import org.springframework.hateoas.EntityModel;

import com.github.antoinecheron.hypermedia.resource.EntityResource;
import com.github.antoinecheron.hypermedia.resource.Resource;

public final class EntityResourceBuilder<T, C> extends ResourceBuilder<T, C, EntityModel<T>> {

  private EntityResourceBuilder(Class<T> resourceType, Class<C> controllerClass) {
    super(resourceType, controllerClass);
  }

  public static <A, C> EntityResourceBuilder<A, C> of(Class<A> resourceType, Class<C> controllerClass) {
    return new EntityResourceBuilder<>(resourceType, controllerClass);
  }

  protected Resource<T, EntityModel<T>> build(ResourceBuilder.ExternalLinks externalLinksBuilder) {
    return new EntityResource<>(
      externalLinksBuilder.resourceType,
      externalLinksBuilder.selfLinkBuilder,
      externalLinksBuilder.selfOperationsResolver,
      externalLinksBuilder.internalLinksResolver,
      externalLinksBuilder.externalLinksResolver
    );
  }

}
