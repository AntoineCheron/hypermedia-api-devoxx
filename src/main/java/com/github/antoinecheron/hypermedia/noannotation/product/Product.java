package com.github.antoinecheron.hypermedia.noannotation.product;

public class Product extends ProductWithoutId {

  private final String id;

  public Product(String id, String title, Double price, String thumbnail) {
    super(title, price, thumbnail);
    this.id = id;
  }

  public String getId() {
    return id;
  }

}
