package com.github.antoinecheron.hypermedia.noannotation.product;

import java.io.IOException;

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

    final String title = DeserializationUtils.readTextualNode("title", parser, rootNode);
    final Double price = DeserializationUtils.readDoubleNode("price", parser, rootNode);
    final String thumbnail = DeserializationUtils.readTextualNode("thumbnail", parser, rootNode);

    return new ProductWithoutId(title, price, thumbnail);
  }

}
