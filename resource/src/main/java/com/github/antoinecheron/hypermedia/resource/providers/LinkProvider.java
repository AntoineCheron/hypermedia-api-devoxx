package com.github.antoinecheron.hypermedia.resource.providers;

import java.util.function.Function;

@FunctionalInterface
public interface LinkProvider<T> extends Function<T, String> {}
