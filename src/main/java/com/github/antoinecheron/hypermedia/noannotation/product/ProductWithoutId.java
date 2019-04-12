package com.github.antoinecheron.hypermedia.noannotation.product;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductWithoutId {

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

  public ProductWithoutId(String title, String seller, Double price, Integer availability, Boolean inStock,
                          Optional<Integer> averageSupplyTime, List<String> colors, List<URL> photos,
                          String shortDescription, Optional<String> detailedDescription, Map<String, String> specifications,
                          List<String> frequentlySoldWithProductId, List<String> sponsoredProductIds,
                          List<String> customersAlsoShoppedForProductIds
  ) {
    this.title = title;
    this.seller = seller;
    this.price = price;
    this.availability = availability;
    this.inStock = inStock;
    this.averageSupplyTime = averageSupplyTime;
    this.colors = colors;
    this.photos = photos;
    this.shortDescription = shortDescription;
    this.detailedDescription = detailedDescription;
    this.specifications = specifications;
    this.frequentlySoldWithProductId = frequentlySoldWithProductId;
    this.sponsoredProductIds = sponsoredProductIds;
    this.customersAlsoShoppedForProductIds = customersAlsoShoppedForProductIds;
  }

  public String getTitle() {
    return title;
  }

  public String getSeller() {
    return seller;
  }

  public Double getPrice() {
    return price;
  }

  public Integer getAvailability() {
    return availability;
  }

  public Boolean getInStock() {
    return inStock;
  }

  public Optional<Integer> getAverageSupplyTime() {
    return averageSupplyTime;
  }

  public List<String> getColors() {
    return colors;
  }

  public List<URL> getPhotos() {
    return photos;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public Optional<String> getDetailedDescription() {
    return detailedDescription;
  }

  public Map<String, String> getSpecifications() {
    return specifications;
  }

  public List<String> getFrequentlySoldWithProductId() {
    return frequentlySoldWithProductId;
  }

  public List<String> getSponsoredProductIds() {
    return sponsoredProductIds;
  }

  public List<String> getCustomersAlsoShoppedForProductIds() {
    return customersAlsoShoppedForProductIds;
  }

  public Product toProduct(String id) {
    return new Product(id, title, seller, price, availability, inStock, averageSupplyTime, colors, photos,
      shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds,
      customersAlsoShoppedForProductIds);
  }

}
