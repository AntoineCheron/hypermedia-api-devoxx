package com.github.antoinecheron.hypermedia.annotated.process;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/process")
public class ProcessApi {

  private final ProcessRepository processRepository;

  public ProcessApi(ProcessRepository processRepository) {
    this.processRepository = processRepository;
  }

  @GetMapping
  private Flux<ProcessSummary> getAllProcesses() {
    return this.processRepository.list();
  }

  @GetMapping("/{id}")
  private Mono<Process> getOneById(@PathVariable String id) {
    return this.processRepository.findById(id);
  }

  @PutMapping("/{id}")
  private Mono<Process> updateOneById(@PathVariable String id, @RequestBody ProcessCreationForm process) {
    return this.processRepository.updateOneById(process.provideId(id));
  }

  @DeleteMapping
  private Mono<Void> deleteOneById(@PathVariable String id) {
    return this.processRepository
      .deleteOneById(id);
  }

  @PostMapping
  private Mono<Process> createOne(@RequestBody ProcessCreationForm process) {
    return this.processRepository.createOne(process);
  }

}
