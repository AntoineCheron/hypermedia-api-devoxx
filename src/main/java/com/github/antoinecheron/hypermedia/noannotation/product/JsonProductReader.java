package com.github.antoinecheron.hypermedia.noannotation.product;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.antoinecheron.hypermedia.noannotation.json.reader.ApplicationReader;
import com.github.antoinecheron.hypermedia.noannotation.product.Product;

public class JsonProductReader implements ApplicationReader<Product> {

  public Optional<Product> read(String value) {
    try {
      final ObjectMapper JSON = new ObjectMapper();
      final JsonNode node = JSON.readTree(value);

      return Optional.of(new Product(
          node.get("id").asText(),
          node.get("title").asText(),
          node.get("price").asDouble(),
          node.get("thumbnail").asText()
      ));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

}
