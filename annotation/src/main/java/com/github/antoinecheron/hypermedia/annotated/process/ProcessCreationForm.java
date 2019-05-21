package com.github.antoinecheron.hypermedia.annotated.process;

import java.util.Date;
import java.util.Optional;

import lombok.Value;

import com.github.antoinecheron.hypermedia.annotated.abstractions.CreationFormOf;

@Value
public class ProcessCreationForm implements CreationFormOf<Process> {

  private final String name;
  private final String createdBy;
  private final String client;
  private final String advisor;
  private final String manager;
  private final ProcessCategory category;
  private final String description;

  public Process provideId(String id) {
    return new Process(
      id, name, createdBy, client, advisor, manager, category, description, 0, ProcessState.CREATED,
      Optional.empty(), new Date(), Optional.empty()
    );
  }

}