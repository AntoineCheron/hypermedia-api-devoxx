package fr.cheron.antoine.hypermedia.noannotation.repositories.redis.serializer;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import fr.cheron.antoine.hypermedia.noannotation.domain.Product;
import fr.cheron.antoine.hypermedia.noannotation.json.reader.ProductReader;

public class ProductSerializer implements RedisSerializer<Product> {
  @Override
  public byte[] serialize(Product product) throws SerializationException {
    final ObjectMapper JSON = new ObjectMapper();
    try {
      return JSON.writeValueAsBytes(product);
    } catch (JsonProcessingException e) {
      throw new SerializationException(e.getMessage());
    }
  }

  @Override
  public Product deserialize(byte[] bytes) throws SerializationException {
    final var input = new String(bytes, StandardCharsets.UTF_8);
    return ProductReader.read(input)
        .orElseThrow(() -> new SerializationException("Cannot deserialize " + input + " into an instance of Product"));
  }

}
