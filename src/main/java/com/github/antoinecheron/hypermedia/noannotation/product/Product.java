package com.github.antoinecheron.hypermedia.noannotation.product;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Product extends ProductWithoutId {

  private final String id;

  public Product(String id, String title, String seller, Double price, Integer availability, Boolean inStock, Optional<Integer> averageSupplyTime, Collection<String> colors, Collection<URL> photos, String shortDescription, Optional<String> detailedDescription, Map<String, String> specifications, Collection<String> frequentlySoldWithProductId, Collection<String> sponsoredProductIds, Collection<String> customersAlsoShoppedForProductIds) {
    super(title, seller, price, availability, inStock, averageSupplyTime, colors, photos, shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds, customersAlsoShoppedForProductIds);
    this.id = id;
  }

  public String getId() {
    return id;
  }

}
