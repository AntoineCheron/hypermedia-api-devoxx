package com.github.antoinecheron.hypermedia.noannotation.product;

import com.github.antoinecheron.hypermedia.noannotation.abstractions.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, ProductWithoutId, ProductSummary> {

}
