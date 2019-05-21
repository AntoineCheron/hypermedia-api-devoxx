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
  private Flux<ProductSummary> getAllProcesses() {
    return this.productRepository.list();
  }

  @GetMapping("/{id}")
  private Mono<Product> getOneById(@PathVariable String id) {
    return this.productRepository.findById(id);
  }

  @PutMapping("/{id}")
  private Mono<Product> updateOneById(@PathVariable String id, @RequestBody ProductWithoutId process) {
    return this.productRepository.updateOneById(process.provideId(id));
  }

  @DeleteMapping
  private Mono<Void> deleteOneById(@PathVariable String id) {
    return this.productRepository
      .deleteOneById(id).then();
  }

  @PostMapping
  private Mono<Product> createOne(@RequestBody ProductWithoutId process) {
    return this.productRepository.createOne(process);
  }

}
