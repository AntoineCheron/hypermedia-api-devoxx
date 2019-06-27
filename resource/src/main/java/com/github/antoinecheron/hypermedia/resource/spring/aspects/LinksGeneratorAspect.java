package com.github.antoinecheron.hypermedia.resource.spring.aspects;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.CollectionResource;
import com.github.antoinecheron.hypermedia.resource.EntityResource;
import com.github.antoinecheron.hypermedia.resource.Resource;

@Aspect
public class LinksGeneratorAspect implements ApplicationContextAware {

  private ApplicationContext springApplicationContext;

  @Around(
    value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
      "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
      "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
      "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
      "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
      "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)"
  )
  public Object generateLinks(ProceedingJoinPoint joinPoint) throws Throwable {
    final Object result = joinPoint.proceed();

    final ResolvableType joinPointReturnType = ResolvableType
      .forMethodReturnType(((MethodSignature) joinPoint.getSignature()).getMethod());

    if (this.springApplicationContext == null) {
      return result;
    } else if (Flux.class.isAssignableFrom(result.getClass())) {
      return generateLinksForFlux((Flux<Object>) result, joinPointReturnType);
    } else if (Mono.class.isAssignableFrom(result.getClass())) {
      return generateLinksForMono((Mono<Object>) result, joinPointReturnType);
    } else {
      return result;
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.springApplicationContext = applicationContext;
  }

  private Object generateLinksForMono(Mono<Object> result, ResolvableType joinPointReturnType) {
    return result.flatMap(value -> {
      if (RepresentationModel.class.isAssignableFrom(value.getClass())) {
        return Mono.just(value);
      } else if (Collection.class.isAssignableFrom(value.getClass())) {
        final Collection<?> collectionValue = (Collection<?>) value;
        return this.generateCollectionResourceLinks(collectionValue, joinPointReturnType);
      } else {
        return this.generateResourceLinks(value);
      }
    });
  }

  private Object generateLinksForFlux(Flux<Object> result, ResolvableType joinPointReturnType) {
    return result
      .collectList()
      .flatMap(values -> this.generateCollectionResourceLinks(values, joinPointReturnType))
      .flatMap(collectionModel ->
        Flux.fromIterable(
          collectionModel.getContent()
            .stream()
            .map(this::generateResourceLinks)
            .collect(Collectors.toList())
        ).flatMap(Function.identity())
        .collectList()
        .map(content -> new CollectionModel<>(content).add(collectionModel.getLinks()))
      ).flux();
  }

  private <T> Mono<EntityModel<T>> generateResourceLinks(T resourceState) {
    final String[] beanNamesForType = this.springApplicationContext.getBeanNamesForType(
      ResolvableType.forClassWithGenerics(
        Resource.class,
        ResolvableType.forClass(resourceState.getClass()),
        ResolvableType.forClassWithGenerics(EntityModel.class, resourceState.getClass())));

    final Optional<String> maybeBeanName = beanNamesForType.length == 0 ? Optional.empty() : Optional.of(beanNamesForType[0]);

    return maybeBeanName
      .flatMap(resourceClassName ->
        Optional.ofNullable((EntityResource<T>) this.springApplicationContext.getBean(resourceClassName)))
      .map(resource -> resource.representation(resourceState))
      .orElseGet(() -> Mono.just(new EntityModel<>(resourceState)));
  }

  private <T, CT extends Collection<T>> Mono<CollectionModel<T>> generateCollectionResourceLinks(
    CT resourceState, ResolvableType joinPointReturnType
  ) {
    return Optional.ofNullable(joinPointReturnType.getGeneric(0))
      .map(listElementsClass -> this.springApplicationContext.getBeanNamesForType(
        ResolvableType.forClassWithGenerics(
          Resource.class,
          listElementsClass,
          ResolvableType.forClassWithGenerics(CollectionModel.class, listElementsClass.getGeneric(0))))
      )
      .flatMap(beanNamesForType -> beanNamesForType.length == 0 ? Optional.empty() : Optional.of(beanNamesForType[0]))
      .flatMap(resourceClassName ->
        Optional.ofNullable((CollectionResource<T, CT>) this.springApplicationContext.getBean(resourceClassName)))
      .map(resource -> resource.representation(resourceState))
      .orElseGet(() -> Mono.just(new CollectionModel<>(resourceState)));
  }

}
