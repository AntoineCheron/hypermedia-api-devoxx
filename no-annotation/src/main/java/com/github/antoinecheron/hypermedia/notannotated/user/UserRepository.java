package com.github.antoinecheron.hypermedia.notannotated.user;

import reactor.core.publisher.Mono;

public interface UserRepository {

  Mono<String> getPassword(String userId);

  Mono<User> getUser(String userId);

  Mono<User> addUser(UserCreationFormWithRole userToCreate);

}
