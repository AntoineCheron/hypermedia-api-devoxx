package fr.cheron.antoine.hypermedia.noannotation.domain;

public class Product {

  private final String id;
  private final String title;
  private final Double price;
  private final String thumbnail;

  public Product(String id, String title, Double price, String thumbnail) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.thumbnail = thumbnail;
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

  public String getThumbnail() {
    return thumbnail;
  }

}
