package com.github.antoinecheron.hypermedia.resource.spring.aspects;

import java.util.Collection;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.EntityModel;
import reactor.core.publisher.Mono;

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

    if (this.springApplicationContext == null || !(Mono.class.isAssignableFrom(result.getClass()))) {
      return result;
    } else {
      final Mono<Object> partiallyParsedResult = (Mono<Object>) result;

      return partiallyParsedResult.flatMap(value -> {
        if (Collection.class.isAssignableFrom(value.getClass())) {
          // TODO: handle resource which are collections
          return Mono.just(value);
        } else {
          return this.generateResourceLinks(value);
        }
      });
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.springApplicationContext = applicationContext;
  }

  private <T> Mono<EntityModel<T>> generateResourceLinks(T resourceState) {
    final String[] beanNamesForType = this.springApplicationContext.getBeanNamesForType(
      ResolvableType.forClassWithGenerics(Resource.class, resourceState.getClass()));

    return Optional.ofNullable(beanNamesForType[0])
      .flatMap(resourceClassName ->
        Optional.ofNullable((Resource<T>) this.springApplicationContext.getBean(resourceClassName)))
      .map(resource -> resource.entityRepresentation(resourceState))
      .orElseGet(() -> Mono.just(new EntityModel<>(resourceState)));
  }

}
