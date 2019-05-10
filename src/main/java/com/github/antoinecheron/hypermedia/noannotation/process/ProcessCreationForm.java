package com.github.antoinecheron.hypermedia.noannotation.process;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import com.github.antoinecheron.hypermedia.noannotation.abstractions.CreationFormOf;

public class ProcessCreationForm implements CreationFormOf<Process> {

  private final String name;
  private final String createdBy;
  private final String client;
  private final String advisor;
  private final String manager;
  private final ProcessCategory category;
  private final String description;

  public ProcessCreationForm(String name, String createdBy, String client, String advisor, String manager, ProcessCategory category, String description) {
    this.name = name;
    this.createdBy = createdBy;
    this.client = client;
    this.advisor = advisor;
    this.manager = manager;
    this.category = category;
    this.description = description;
  }

  public Process provideId(String id) {
    return new Process(
      id, name, createdBy, client, advisor, manager, category, description,
      0,
      ProcessState.CREATED,
      Optional.empty(),
      new Date(),
      Optional.empty()
    );
  }

  public String getName() {
    return name;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public String getClient() {
    return client;
  }

  public String getAdvisor() {
    return advisor;
  }

  public String getManager() {
    return manager;
  }

  public ProcessCategory getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProcessCreationForm that = (ProcessCreationForm) o;
    return Objects.equals(name, that.name) &&
      Objects.equals(createdBy, that.createdBy) &&
      Objects.equals(client, that.client) &&
      Objects.equals(advisor, that.advisor) &&
      Objects.equals(manager, that.manager) &&
      category == that.category &&
      Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, createdBy, client, advisor, manager, category, description);
  }

  @Override
  public String toString() {
    return "ProcessCreationForm{" +
      "name='" + name + '\'' +
      ", createdBy='" + createdBy + '\'' +
      ", client='" + client + '\'' +
      ", advisor='" + advisor + '\'' +
      ", manager='" + manager + '\'' +
      ", category=" + category +
      ", description='" + description + '\'' +
      '}';
  }

}