package com.github.antoinecheron.hypermedia.noannotation.product;

import java.net.URL;
import java.util.Objects;

public class ProductSummary {

  private final String id;
  private final String title;
  private final Double price;
  private final Boolean inStock;
  private final URL thumbnail;

  public ProductSummary(String id, String title, Double price, Boolean inStock, URL thumbnail) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.inStock = inStock;
    this.thumbnail = thumbnail;
  }

  public static ProductSummary fromProduct(Product product) {
    return new ProductSummary(
      product.getId(), product.getTitle(), product.getPrice(), product.getInStock(), product.getPhotos().get(0)
    );
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Double getPrice() {
    return price;
  }

  public Boolean getInStock() {
    return inStock;
  }

  public URL getThumbnail() {
    return thumbnail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductSummary that = (ProductSummary) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(title, that.title) &&
      Objects.equals(price, that.price) &&
      Objects.equals(inStock, that.inStock) &&
      Objects.equals(thumbnail, that.thumbnail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, price, inStock, thumbnail);
  }

}
