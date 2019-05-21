package com.github.antoinecheron.hypermedia.annotated.configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Config {

  private Config() {}

  public static final Scheduler APPLICATION_SCHEDULER = Schedulers.elastic();

}
