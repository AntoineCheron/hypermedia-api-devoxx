package com.github.antoinecheron.hypermedia.annotated.product;

import com.github.antoinecheron.hypermedia.annotated.abstractions.CrudApi;

public class ProductApi extends CrudApi<Product, ProductWithoutId, ProductSummary, ProductRepository> {

  public ProductApi(ProductRepository service) {
    super(service, Product.class, ProductWithoutId.class, ProductSummary.class);
  }

}
