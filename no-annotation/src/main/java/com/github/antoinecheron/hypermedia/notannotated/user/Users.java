package com.github.antoinecheron.hypermedia.notannotated.user;

public class Users {

  public static User fromUserCreationForm(UserCreationFormWithRole userToCreate, String id) {
    return new User(id, userToCreate.getLastName(), userToCreate.getFirstName(), userToCreate.getEmail(), userToCreate.getRole());
  }

}
