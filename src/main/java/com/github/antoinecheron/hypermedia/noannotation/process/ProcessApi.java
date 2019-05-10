package com.github.antoinecheron.hypermedia.noannotation.process;

import com.github.antoinecheron.hypermedia.noannotation.abstractions.CrudApi;

public class ProcessApi extends CrudApi<Process, ProcessCreationForm, ProcessSummary, ProcessRepository> {

  public ProcessApi(ProcessRepository service) {
    super(service, Process.class, ProcessCreationForm.class, ProcessSummary.class);
  }

}
