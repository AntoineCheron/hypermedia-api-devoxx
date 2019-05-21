package com.github.antoinecheron.hypermedia.annotated.process;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document(collection = "process")
class ProcessSummary {

  @Id
  private final String id;
  private final String name;
  private final String client;
  private final String advisor;
  private final ProcessCategory category;
  private final ProcessState state;

}
