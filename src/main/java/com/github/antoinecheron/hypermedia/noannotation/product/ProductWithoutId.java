package com.github.antoinecheron.hypermedia.noannotation.product;

public class ProductWithoutId {

  protected final String title;
  protected final Double price;
  protected final String thumbnail;

  public ProductWithoutId(String title, Double price, String thumbnail) {
    this.title = title;
    this.price = price;
    this.thumbnail = thumbnail;
  }

  public String getTitle() {
    return title;
  }

  public Double getPrice() {
    return price;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public Product toProduct(String id) {
    return new Product(id, title, price, thumbnail);
  }

}
