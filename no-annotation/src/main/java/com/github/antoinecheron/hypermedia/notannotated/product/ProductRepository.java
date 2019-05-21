package com.github.antoinecheron.hypermedia.notannotated.product;

import com.github.antoinecheron.hypermedia.notannotated.abstractions.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, ProductWithoutId, ProductSummary> {

}
