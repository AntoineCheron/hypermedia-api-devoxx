package com.github.antoinecheron.hypermedia.annotated.product;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Value;
import lombok.experimental.NonFinal;

import com.github.antoinecheron.hypermedia.annotated.abstractions.CreationFormOf;

@Value
@NonFinal
public class ProductWithoutId implements CreationFormOf<Product> {

  protected final String title;
  protected final String seller;
  protected final Double price;
  protected final Integer availability;
  protected final Boolean inStock;
  protected final Optional<Integer> averageSupplyTime;
  protected final List<String> colors;
  protected final List<URL> photos;
  protected final String shortDescription;
  protected final Optional<String> detailedDescription;
  protected final Map<String, String> specifications;
  protected final List<String> frequentlySoldWithProductId;
  protected final List<String> sponsoredProductIds;
  protected final List<String> customersAlsoShoppedForProductIds;

  public Product provideId(String id) {
    return new Product(id, title, seller, price, availability, inStock, averageSupplyTime, colors, photos,
      shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds,
      customersAlsoShoppedForProductIds);
  }

}
