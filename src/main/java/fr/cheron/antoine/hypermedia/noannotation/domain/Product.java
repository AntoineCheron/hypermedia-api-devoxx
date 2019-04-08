package fr.cheron.antoine.hypermedia.noannotation.domain;

public class Product {

  private final String id;
  private final String title;
  private final Double price;

  public Product(String id, String title, Double price) {
    this.id = id;
    this.title = title;
    this.price = price;
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

}
