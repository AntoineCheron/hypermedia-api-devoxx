package com.github.antoinecheron.hypermedia.annotated.process;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Process {

  private final String id;
  private final String name;
  private final String createdBy;
  private final String client;
  private final String advisor;
  private final String manager;
  private final ProcessCategory category;
  private final String description;
  private final Integer step;
  private final ProcessState state;
  private final Optional<Date> startDate;
  private final Date lastUpdate;
  private final Optional<Date> endDate;

  public Process(String id, String name, String createdBy, String client, String advisor, String manager, ProcessCategory category, String description, Integer step, ProcessState state, Optional<Date> startDate, Date lastUpdate, Optional<Date> endDate) {
    this.id = id;
    this.name = name;
    this.createdBy = createdBy;
    this.client = client;
    this.advisor = advisor;
    this.manager = manager;
    this.category = category;
    this.description = description;
    this.step = step;
    this.state = state;
    this.startDate = startDate;
    this.lastUpdate = lastUpdate;
    this.endDate = endDate;
  }

  public static Process fromProcessCreationForm(ProcessCreationForm processCreationForm, String id, Integer step, ProcessState state, Optional<Date> startDate, Date lastUpdate, Optional<Date> endDate) {
    return new Process(
      id,
      processCreationForm.getName(),
      processCreationForm.getCreatedBy(),
      processCreationForm.getClient(),
      processCreationForm.getAdvisor(),
      processCreationForm.getManager(),
      processCreationForm.getCategory(),
      processCreationForm.getDescription(),
      step,
      state,
      startDate,
      lastUpdate,
      endDate
    );
  }

  public String getId() {
    return id;
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

  public Integer getStep() {
    return step;
  }

  public ProcessState getState() {
    return state;
  }

  public Optional<Date> getStartDate() {
    return startDate;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public Optional<Date> getEndDate() {
    return endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Process process = (Process) o;
    return Objects.equals(id, process.id) &&
      Objects.equals(name, process.name) &&
      Objects.equals(createdBy, process.createdBy) &&
      Objects.equals(client, process.client) &&
      Objects.equals(advisor, process.advisor) &&
      Objects.equals(manager, process.manager) &&
      category == process.category &&
      Objects.equals(description, process.description) &&
      Objects.equals(step, process.step) &&
      state == process.state &&
      Objects.equals(startDate, process.startDate) &&
      Objects.equals(lastUpdate, process.lastUpdate) &&
      Objects.equals(endDate, process.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, createdBy, client, advisor, manager, category, description, step, state, startDate, lastUpdate, endDate);
  }

  @Override
  public String toString() {
    return "Process{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", createdBy='" + createdBy + '\'' +
      ", client='" + client + '\'' +
      ", advisor='" + advisor + '\'' +
      ", manager='" + manager + '\'' +
      ", category=" + category +
      ", description='" + description + '\'' +
      ", step=" + step +
      ", state=" + state +
      ", startDate=" + startDate +
      ", lastUpdate=" + lastUpdate +
      ", endDate=" + endDate +
      '}';
  }

}
