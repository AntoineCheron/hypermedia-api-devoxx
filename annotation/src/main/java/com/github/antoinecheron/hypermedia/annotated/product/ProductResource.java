package com.github.antoinecheron.hypermedia.annotated.product;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.github.antoinecheron.hypermedia.resource.providers.LinkProvider;
import com.github.antoinecheron.hypermedia.resource.providers.OperationProvider;
import com.github.antoinecheron.hypermedia.resource.Resource;

@Component
public class ProductResource {

  @Bean
  public Resource<Product> getProductResource() {
    return Resource.builder(Product.class, ProductApi.class)
      .withSelfLink(selfLink)
      .withOperations()
        .operation("update", this::isProductIdSumAMultipleOf2, update)
        .operation("delete", delete)
      .withExternalLinks()
        .toUrlAlwaysAvailable("amazon", "https://www.amazon.fr/")
        .toUrlAlwaysAvailable("searchOnAmazon", searchOnAmazon)
      .build();
  }

  private boolean isProductIdSumAMultipleOf2(Product product) {
    return product.getId().chars().sum() % 2 == 0;
  }

  private final OperationProvider<Product, ProductApi> selfLink = (product, api) ->
    api.getOneById(product.getId());

  private final OperationProvider<Product, ProductApi> update = (product, api) ->
    api.updateOneById(product.getId(), null);

  private final OperationProvider<Product, ProductApi> delete = (product, api) ->
    api.deleteOneById(product.getId());

  private final LinkProvider<Product> searchOnAmazon = product ->
    "https://www.amazon.fr/s?k=" + product.getTitle().replace(' ', '+');

}
