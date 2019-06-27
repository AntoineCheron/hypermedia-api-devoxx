package com.github.antoinecheron.hypermedia.annotated.product;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.github.antoinecheron.hypermedia.resource.providers.LinkProvider;
import com.github.antoinecheron.hypermedia.resource.providers.OperationProvider;
import com.github.antoinecheron.hypermedia.resource.Resource;

@Component
public class ProductResource {

  @Bean
  public Resource<Product, EntityModel<Product>> getProductResource() {
    return Resource.entityBuilder(Product.class, ProductApi.class)
      .withSelfLink(singleProductSelfLink)
      .withOperations()
        .operation("update", this::isProductIdSumAMultipleOf2, update)
        .operation("delete", delete)
      .withExternalLinks()
        .toUrlAlwaysAvailable("amazon", "https://www.amazon.fr/")
        .toUrlAlwaysAvailable("searchOnAmazon", searchOnAmazon)
      .build();
  }

  @Bean
  public Resource<List<ProductSummary>, CollectionModel<ProductSummary>> getProductsResource() {
      return Resource.collectionBuilder(Collections.<ProductSummary>emptyList().getClass(), ProductApi.class)
        .withSelfLink(allProductsSelfLink)
        .withOperations()
          .operation("create_new_product", create)
        .build();
  }

  private boolean isProductIdSumAMultipleOf2(Product product) {
    return product.getId().chars().sum() % 2 == 0;
  }

  private final OperationProvider<Product, ProductApi> singleProductSelfLink = (product, api) ->
    api.getOneById(product.getId());

  private final OperationProvider<List<ProductSummary>, ProductApi> allProductsSelfLink = (products, api) ->
    api.getAllProcesses();

  private final OperationProvider<Product, ProductApi> update = (product, api) ->
    api.updateOneById(product.getId(), null);

  private final OperationProvider<Product, ProductApi> delete = (product, api) ->
    api.deleteOneById(product.getId());

  private final LinkProvider<Product> searchOnAmazon = product ->
    "https://www.amazon.fr/s?k=" + product.getTitle().replace(' ', '+');

  private final OperationProvider<List<Product>, ProductApi> create = (products, api) -> api.createOne(null);

}
