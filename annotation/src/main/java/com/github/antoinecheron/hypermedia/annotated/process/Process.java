package com.github.antoinecheron.hypermedia.annotated.process;

import java.util.Date;
import java.util.Optional;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document(collection = "process")
public class Process {

  @Id
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

  static Process fromProcessCreationForm(ProcessCreationForm processCreationForm, String id, Integer step, ProcessState state, Optional<Date> startDate, Date lastUpdate, Optional<Date> endDate) {
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

}
