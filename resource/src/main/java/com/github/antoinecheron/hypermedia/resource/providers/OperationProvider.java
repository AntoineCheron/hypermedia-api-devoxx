package com.github.antoinecheron.hypermedia.resource.providers;

import java.util.function.BiFunction;

@FunctionalInterface
public interface OperationProvider<R, C> extends BiFunction<R, C, Object> { }
