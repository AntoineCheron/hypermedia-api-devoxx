package com.github.antoinecheron.hypermedia.annotated.product;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.antoinecheron.hypermedia.annotated.utils.DeserializationUtils;

public class ProductDeserializer extends StdDeserializer<Product> {

  public ProductDeserializer() {
    super(Product.class);
  }

  @Override
  public Product deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final JsonNode rootNode = parser.getCodec().readTree(parser);

    final var productWithoutId = rootNode.traverse(parser.getCodec()).readValueAs(ProductWithoutId.class);
    final var id = DeserializationUtils.Strings.required("id", parser, rootNode);

    return productWithoutId.provideId(id);
  }

}
