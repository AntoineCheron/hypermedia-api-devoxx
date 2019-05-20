package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import com.github.antoinecheron.hypermedia.notannotated.Config;
import com.github.antoinecheron.hypermedia.notannotated.security.PasswordHashingService;
import com.github.antoinecheron.hypermedia.notannotated.utils.MongoUtils;

public class ReactiveMongoUserRepository implements UserRepository {

  private static final String USER_COLLECTION_NAME = "users";
  private static final String CREDENTIALS_COLLECTION_NAME = "credentials";

  private final ReactiveMongoOperations mongoOperations;


  public ReactiveMongoUserRepository(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Mono<String> getPassword(String userId) {
    return mongoOperations.findOne(
      MongoUtils.idQuery(userId),
      UserCredentials.class,
      CREDENTIALS_COLLECTION_NAME
    )
      .map(UserCredentials::getPassword)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<User> getUser(String userId) {
    return mongoOperations.findOne(
      MongoUtils.idQuery(userId),
      User.class,
      USER_COLLECTION_NAME
    ).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

  @Override
  public Mono<User> addUser(UserCreationFormWithRole userToCreate) {
    return this.mongoOperations.inTransaction().execute(action -> {
      final var userId = UUID.randomUUID().toString();

      final var credentialsCreation = Mono
        .fromCallable(() -> PasswordHashingService.generateStrongPasswordHash(userToCreate.getPassword()))
        .map(password -> new UserCredentials(userId, password))
        .flatMap(credentials -> action.insert(credentials, CREDENTIALS_COLLECTION_NAME));

      final var userCreation = action.insert(
        Users.fromUserCreationForm(userToCreate, userId),
        USER_COLLECTION_NAME
      );

      return Mono.zip(credentialsCreation, userCreation);
    }).next()
      .map(Tuple2::getT2)
      .subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}
