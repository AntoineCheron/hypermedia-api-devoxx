package com.github.antoinecheron.hypermedia.annotated.product;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.github.antoinecheron.hypermedia.resource.Resource;

@Component
public class ProductResource {

  @Bean
  public Resource<Product> getProductResource() {
    return Resource.builder(Product.class, ProductApi.class)
      .self((product, api) -> api.getOneById(product.getId()))
      .withOperations()
        .availableIf(
          "update",
          this::isProductIdSumMutiple2,
          (product, api) -> api.updateOneById(product.getId(), null)
        )
        .alwaysAvailable("delete", (product, api) -> api.deleteOneById(product.getId()))
      .withExternalLinks()
        .toUrlAlwaysAvailable("amazon", "https://www.amazon.fr/")
        .toUrlAlwaysAvailable(
          "searchOnAmazon",
          product -> "https://www.amazon.fr/s?k=" + product.getTitle().replace(' ', '+')
        )
      .build();
  }

  private boolean isProductIdSumMutiple2(Product product) {
    return product.getId().chars().sum() % 2 == 0;
  }

}
