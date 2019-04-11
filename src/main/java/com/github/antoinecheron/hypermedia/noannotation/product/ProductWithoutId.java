package com.github.antoinecheron.hypermedia.noannotation.product;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ProductWithoutId {

  protected final String title;
  protected final String seller;
  protected final Double price;
  protected final Integer availability;
  protected final Boolean inStock;
  protected final Optional<Integer> averageSupplyTime;
  protected final Collection<String> colors;
  protected final Collection<URL> photos;
  protected final String shortDescription;
  protected final Optional<String> detailedDescription;
  protected final Map<String, String> specifications;
  protected final Collection<String> frequentlySoldWithProductId;
  protected final Collection<String> sponsoredProductIds;
  protected final Collection<String> customersAlsoShoppedForProductIds;

  public ProductWithoutId(String title, String seller, Double price, Integer availability, Boolean inStock,
                          Optional<Integer> averageSupplyTime, Collection<String> colors, Collection<URL> photos,
                          String shortDescription, Optional<String> detailedDescription, Map<String, String> specifications,
                          Collection<String> frequentlySoldWithProductId, Collection<String> sponsoredProductIds,
                          Collection<String> customersAlsoShoppedForProductIds
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

  public Collection<String> getColors() {
    return colors;
  }

  public Collection<URL> getPhotos() {
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

  public Collection<String> getFrequentlySoldWithProductId() {
    return frequentlySoldWithProductId;
  }

  public Collection<String> getSponsoredProductIds() {
    return sponsoredProductIds;
  }

  public Collection<String> getCustomersAlsoShoppedForProductIds() {
    return customersAlsoShoppedForProductIds;
  }

  public Product toProduct(String id) {
    return new Product(id, title, seller, price, availability, inStock, averageSupplyTime, colors, photos,
      shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds,
      customersAlsoShoppedForProductIds);
  }

}
