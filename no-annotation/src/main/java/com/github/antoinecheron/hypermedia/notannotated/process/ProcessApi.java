package com.github.antoinecheron.hypermedia.notannotated.process;

import com.github.antoinecheron.hypermedia.notannotated.abstractions.CrudApi;

public class ProcessApi extends CrudApi<Process, ProcessCreationForm, ProcessSummary, ProcessRepository> {

  public ProcessApi(ProcessRepository service) {
    super(service, Process.class, ProcessCreationForm.class, ProcessSummary.class);
  }

}
