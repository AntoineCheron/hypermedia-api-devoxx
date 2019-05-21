package com.github.antoinecheron.hypermedia.notannotated.process;

import java.util.Objects;

public class ProcessSummary {

  private final String id;
  private final String name;
  private final String client;
  private final String advisor;
  private final ProcessCategory category;
  private final ProcessState state;

  public ProcessSummary(String id, String name, String client, String advisor, ProcessCategory category, ProcessState state) {
    this.id = id;
    this.name = name;
    this.client = client;
    this.advisor = advisor;
    this.category = category;
    this.state = state;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getClient() {
    return client;
  }

  public String getAdvisor() {
    return advisor;
  }

  public ProcessCategory getCategory() {
    return category;
  }

  public ProcessState getState() {
    return state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProcessSummary that = (ProcessSummary) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(client, that.client) &&
      Objects.equals(advisor, that.advisor) &&
      category == that.category &&
      state == that.state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, client, advisor, category, state);
  }

  @Override
  public String toString() {
    return "ProcessSummary{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", client='" + client + '\'' +
      ", advisor='" + advisor + '\'' +
      ", category=" + category +
      ", state=" + state +
      '}';
  }

}
