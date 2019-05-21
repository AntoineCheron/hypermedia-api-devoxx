package com.github.antoinecheron.hypermedia.annotated.product;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@EqualsAndHashCode(callSuper=true)
@Document(collection = "products")
public class Product extends ProductWithoutId {

  @Id
  private final String id;

  Product(String id, String title, String seller, Double price, Integer availability, Boolean inStock, Optional<Integer> averageSupplyTime, List<String> colors, List<URL> photos, String shortDescription, Optional<String> detailedDescription, Map<String, String> specifications, List<String> frequentlySoldWithProductId, List<String> sponsoredProductIds, List<String> customersAlsoShoppedForProductIds) {
    super(title, seller, price, availability, inStock, averageSupplyTime, colors, photos, shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds, customersAlsoShoppedForProductIds);
    this.id = id;
  }

}
