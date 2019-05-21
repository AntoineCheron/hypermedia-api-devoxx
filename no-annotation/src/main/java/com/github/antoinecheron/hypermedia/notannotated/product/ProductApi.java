package com.github.antoinecheron.hypermedia.notannotated.product;

import com.github.antoinecheron.hypermedia.notannotated.abstractions.CrudApi;

public class ProductApi extends CrudApi<Product, ProductWithoutId, ProductSummary, ProductRepository> {

  public ProductApi(ProductRepository service) {
    super(service, Product.class, ProductWithoutId.class, ProductSummary.class);
  }

}
