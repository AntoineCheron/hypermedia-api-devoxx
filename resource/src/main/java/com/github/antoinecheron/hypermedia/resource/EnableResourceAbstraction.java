package com.github.antoinecheron.hypermedia.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(com.github.antoinecheron.hypermedia.resource.ResourceConfiguration.class)
public @interface EnableResourceAbstraction {}
