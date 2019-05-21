package com.github.antoinecheron.hypermedia.annotated.product;

import java.net.URL;
import java.util.List;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document(collection = "products")
public class ProductSummary {

  @Id
  private final String id;
  private final String title;
  private final Double price;
  private final Boolean inStock;
  private final List<URL> photos;

  public static ProductSummary fromProduct(Product product) {
    return new ProductSummary(
      product.getId(), product.getTitle(), product.getPrice(), product.getInStock(), product.getPhotos()
    );
  }

}
