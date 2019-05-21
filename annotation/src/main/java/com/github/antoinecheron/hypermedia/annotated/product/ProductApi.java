package com.github.antoinecheron.hypermedia.annotated.product;

import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.github.antoinecheron.hypermedia.resource.Resource;
import com.github.antoinecheron.hypermedia.resource.WithResource;

@RestController
@RequestMapping("/products")
public class ProductApi implements WithResource<Product> {

  private final ProductRepository productRepository;
  @Getter private final Resource<Product> resource;

  public ProductApi(ProductRepository productRepository, Resource<Product> productResource) {
    this.productRepository = productRepository;
    this.resource = productResource;
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
  public Mono<Product> createOne(@RequestBody ProductWithoutId process) {
    return this.productRepository.createOne(process);
  }

}
