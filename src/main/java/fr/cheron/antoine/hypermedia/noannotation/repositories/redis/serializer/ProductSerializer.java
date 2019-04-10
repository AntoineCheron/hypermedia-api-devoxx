package fr.cheron.antoine.hypermedia.noannotation.repositories.redis.serializer;

import fr.cheron.antoine.hypermedia.noannotation.domain.Product;
import fr.cheron.antoine.hypermedia.noannotation.json.reader.ProductReader;

public class ProductSerializer extends AbstractRedisSerializer<Product> {

  public ProductSerializer() {
    super(new ProductReader());
  }

}
