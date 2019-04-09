package fr.cheron.antoine.hypermedia.noannotation.repositories.redis.implem;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import fr.cheron.antoine.hypermedia.noannotation.Config;
import fr.cheron.antoine.hypermedia.noannotation.domain.Product;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.ForbiddenResourceOverrideException;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.NotFoundResourceException;
import fr.cheron.antoine.hypermedia.noannotation.repositories.ProductRepository;
import fr.cheron.antoine.hypermedia.noannotation.repositories.redis.serializer.ProductSerializer;

public class RedisProductRepository implements ProductRepository {

  private final ReactiveRedisOperations<String, Product> redisOperations;

  public RedisProductRepository(ReactiveRedisConnectionFactory connectionFactory) {
    final var serializer = new ProductSerializer();
    final var serializationContextBuilder = RedisSerializationContext.<String, Product>newSerializationContext(new StringRedisSerializer());
    final var context = serializationContextBuilder.value(serializer).build();
    this.redisOperations = new ReactiveRedisTemplate<>(connectionFactory, context);
  }

  public Flux<Product> list() {
    return this.redisOperations.keys("*")
        .flatMap(redisOperations.opsForValue()::get)
        .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Product> findById(String id) {
    return this.redisOperations.opsForValue().get(id)
        .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> deleteOneById(String id) {
    return this.redisOperations.opsForValue().delete(id)
      .flatMap((succeeded) ->
          succeeded ? Mono.just(true) : NotFoundResourceException.asMono("product " + id)
      ).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> createOne(Product product) {
    return this.redisOperations.opsForValue()
        .setIfAbsent(product.getId(), product)
        .flatMap((set) -> {
          if (!set) return ForbiddenResourceOverrideException.asMono();
          else return Mono.just(true);
        }).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> updateOneById(Product product) {
    return this.redisOperations.opsForValue()
        .setIfPresent(product.getId(), product)
        .flatMap((set) ->
            set ? Mono.just(true) : NotFoundResourceException.asMono("product " + product.getId())
        ).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}
