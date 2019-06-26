package com.github.antoinecheron.hypermedia.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.github.antoinecheron.hypermedia.resource.aspects.LinksGeneratorAspect;

@Configuration
@EnableAspectJAutoProxy
public class ResourceConfiguration {

  @Bean
  public LinksGeneratorAspect linksGeneratorAspect() {
    return new LinksGeneratorAspect();
  }

}
