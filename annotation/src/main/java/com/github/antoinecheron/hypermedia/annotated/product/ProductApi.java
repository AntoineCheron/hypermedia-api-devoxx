package com.github.antoinecheron.hypermedia.annotated.product;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductApi {

  private final ProductRepository productRepository;

  public ProductApi(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping
  public Flux<ProductSummary> getAllProcesses() {
    return this.productRepository.list();
  }

  @GetMapping("/{id}")
  public Mono<Product> getOneById(@PathVariable String id) {
    return this.productRepository.findById(id);
  }

  @PutMapping("/{id}")
  public Mono<Product> updateOneById(@PathVariable String id, @RequestBody ProductWithoutId process) {
    return this.productRepository.updateOneById(process.provideId(id));
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteOneById(@PathVariable String id) {
    return this.productRepository
      .deleteOneById(id).then();
  }

  @PostMapping
  public Mono<? extends Object> createOne(@RequestBody ProductWithoutId process) {
    return this.productRepository.createOne(process);
  }

}
