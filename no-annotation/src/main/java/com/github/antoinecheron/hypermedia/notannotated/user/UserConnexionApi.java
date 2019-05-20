package com.github.antoinecheron.hypermedia.notannotated.user;

import java.net.URI;
import java.util.Optional;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import com.github.antoinecheron.hypermedia.notannotated.exceptions.WrongCredentialsException;
import com.github.antoinecheron.hypermedia.notannotated.json.JsonWriter;
import com.github.antoinecheron.hypermedia.notannotated.security.AuthenticationHelper;
import com.github.antoinecheron.hypermedia.notannotated.security.JsonWebTokenService;
import com.github.antoinecheron.hypermedia.notannotated.security.PasswordHashingService;
import com.github.antoinecheron.hypermedia.notannotated.utils.Responses;

public class UserConnexionApi {

  public static final String DEFAULT_LOGIN_PATH = "/login";
  public static final String DEFAULT_REGISTRATION_PATH = "/register";

  private final UserRepository userRepository;

  public UserConnexionApi(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(UserCredentials.class)
      .flatMap(this::completeWithPasswordFromDB)
      .flatMap(userCredentialsAndPasswordTuple ->
        this.validatePassword(userCredentialsAndPasswordTuple.getT1(), userCredentialsAndPasswordTuple.getT2())
      )
      .flatMap(credentials -> this.userRepository.getUser(credentials.getUserId()))
      .map(JsonWebTokenService::createToken)
      .flatMap(Responses::loginOk);
  }

  public Mono<ServerResponse> register(ServerRequest request) {
    return AuthenticationHelper.withAuthenticationOptional(request, maybeUser ->
      parseCreationFormRequestBodyAndAssignValidRole(request, maybeUser)
        .filter(userToCreate -> userToCreate.getPassword().equals(userToCreate.getPasswordConfirm()))
        .flatMap(userRepository::addUser)
        .flatMap(user -> JsonWriter.write(user).flatMap(json ->
          Responses.created(URI.create("/user/" + user.getId()), json)) // TODO : use a generator for the link to the user
        )
    );
  }

  private Mono<Tuple2<UserCredentials, String>> completeWithPasswordFromDB(UserCredentials userCredentials) {
    final var password = this.userRepository
      .getPassword(userCredentials.getUserId())
      .switchIfEmpty(Mono.error(WrongCredentialsException::new));

    return Mono.zip(Mono.just(userCredentials), password);
  }

  private Mono<UserCredentials> validatePassword(UserCredentials userCredentials, String password) {
    return Mono.fromCallable(() -> PasswordHashingService.validatePassword(
      userCredentials.getPassword(),
      password
    ))
      .filter(success -> success)
      .switchIfEmpty(Mono.error(WrongCredentialsException::new))
      .map(success -> userCredentials);
  }

  private Mono<UserCreationFormWithRole> parseCreationFormRequestBodyAndAssignValidRole(ServerRequest request, Optional<User> maybeLoggedInUser) {
    return maybeLoggedInUser.map(user ->
      request.bodyToMono(UserCreationFormWithRole.class)
        .filter(creationFormWithRole -> UserBusinessRules.isRoleAssignmentAuthorized(user, creationFormWithRole))
        .switchIfEmpty(parseCreationFormWithoutRoleBodyAndAssignDefaultRole(request, Optional.of(user.getRole())))
        .onErrorResume(
          ServerWebInputException.class,
          ignoredException -> parseCreationFormWithoutRoleBodyAndAssignDefaultRole(request, Optional.of(user.getRole()))
        )
    ).orElseGet(() -> parseCreationFormWithoutRoleBodyAndAssignDefaultRole(request, Optional.empty()));
  }

  private Mono<UserCreationFormWithRole> parseCreationFormWithoutRoleBodyAndAssignDefaultRole(ServerRequest request, Optional<UserRole> maybeCreatorRole) {
    return request.bodyToMono(UserCreationFormWithoutRole.class)
      .map(userCreationFormWithoutRole -> userCreationFormWithoutRole
        .withRole(UserBusinessRules.getDefaultRoleToAssign(maybeCreatorRole))
      );
  }


}
