package com.github.antoinecheron.hypermedia.resource;

import org.springframework.hateoas.Link;

@FunctionalInterface
public interface LinkBuilder<T> {

  Link build(T resourceState);

}
