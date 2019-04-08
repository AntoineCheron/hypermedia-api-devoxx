package fr.cheron.antoine.hypermedia.noannotation.services;

import fr.cheron.antoine.hypermedia.noannotation.Config;
import fr.cheron.antoine.hypermedia.noannotation.domain.Product;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.ForbiddenResourceOverrideException;
import fr.cheron.antoine.hypermedia.noannotation.exceptions.NotFoundResourceException;
import fr.cheron.antoine.hypermedia.noannotation.utils.MonoUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductService {

  private final List<Product> products;

  public ProductService() {
    this.products = Stream.of(
        new Product("1", "Lampe Foo", 50d),
        new Product("2", "Table Toto", 30d),
        new Product("3", "Papa John", 30d),
        new Product("4", "John D'eau", 16d)
    ).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
  }

  public Flux<Product> list() {
    return Flux.fromIterable(this.products).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Product> findById(String id) {
    return Mono.fromSupplier(() -> this.products). // to make sure nothing gets executed until one subscribes to the mono
        flatMap((products) -> MonoUtils.fromOptionalWithNotFoundException(
        products.stream().filter((product) -> product.getId().equals(id)).findFirst(),
        "product " + id
    )).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> deleteOneById(String id) {
    return this.findById(id).
        map(this.products::remove).
        flatMap((succeeded) ->
            succeeded ? Mono.just(true) : Mono.error(new NotFoundResourceException("product " + id))
        ).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> createOne(Product product) {
    return this.findById(product.getId()).
        flatMap((notUsed) -> Mono.<Boolean>error(new ForbiddenResourceOverrideException())).
        onErrorResume(
            NotFoundResourceException.class,
            (notUsedException) -> Mono.fromCallable(() -> {
              this.products.add(product);
              return true;
            })
        ).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  public Mono<Boolean> updateOneById(Product product) {
    return this.findById(product.getId()).
        map((previous) -> {
          this.products.remove(previous);
          this.products.add(product);
          return true;
        }).
        subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}
