package com.github.antoinecheron.hypermedia.noannotation.product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.antoinecheron.hypermedia.noannotation.utils.DeserializationUtils;

public class ProductWithoutIdDeserializer extends StdDeserializer<ProductWithoutId> {

  public ProductWithoutIdDeserializer() {
    super(ProductWithoutId.class);
  }

  @Override
  public ProductWithoutId deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

    final JsonNode rootNode = parser.getCodec().readTree(parser);

    final String title = DeserializationUtils.Strings.required("title", parser, rootNode);
    final String seller = DeserializationUtils.Strings.required("seller", parser, rootNode);
    final Double price = DeserializationUtils.Doubles.required("price", parser, rootNode);
    final Integer availability = DeserializationUtils.Integers.required("availability", parser, rootNode);
    final Boolean inStock = DeserializationUtils.Booleans.required("inStock", parser, rootNode);
    final Optional<Integer> averageSupplyTime = DeserializationUtils.Integers.optional("averageSupplyTime", rootNode);
    final List<String> colors = DeserializationUtils.Collections.string("colors", rootNode);
    final List<URL> photos = DeserializationUtils.Collections.string("photos", rootNode)
      .parallelStream().flatMap((photoUrl) -> {
        try { return Stream.of(new URL(photoUrl)); }
        catch (MalformedURLException e) { return Stream.empty(); }
      }).collect(Collectors.toList());
    final String shortDescription = DeserializationUtils.Strings.required("shortDescription", parser, rootNode);
    final Optional<String> detailedDescription = DeserializationUtils.Strings.optional("detailedDescription", rootNode);
    final Map<String, String> specifications = DeserializationUtils.Maps.asMapString("specifications", rootNode);
    final List<String> frequentlySoldWithProductId = DeserializationUtils.Collections.string("frequentlySoldWithProductId", rootNode);
    final List<String> sponsoredProductIds = DeserializationUtils.Collections.string("sponsoredProductIds", rootNode);
    final List<String> customersAlsoShoppedForProductIds = DeserializationUtils.Collections.string("customersAlsoShoppedForProductIds", rootNode);

    return new ProductWithoutId(title, seller, price, availability, inStock, averageSupplyTime, colors, photos,
      shortDescription, detailedDescription, specifications, frequentlySoldWithProductId, sponsoredProductIds,
      customersAlsoShoppedForProductIds);
  }

}
