package com.github.antoinecheron.hypermedia.annotated.product;

import com.github.antoinecheron.hypermedia.annotated.abstractions.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, ProductWithoutId, ProductSummary> {

}
