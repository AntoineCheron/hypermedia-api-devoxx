package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.Objects;

public class User {

  private final String id;
  private final String lastName;
  private final String firstName;
  private final String email;
  private final UserRole role;

  public User(String id, String lastName, String firstName, String email, UserRole role) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.role = role;
  }

  public String getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getEmail() {
    return email;
  }

  public UserRole getRole() {
    return role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) &&
      Objects.equals(lastName, user.lastName) &&
      Objects.equals(firstName, user.firstName) &&
      Objects.equals(email, user.email) &&
      role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, lastName, firstName, email, role);
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", lastName='" + lastName + '\'' +
      ", firstName='" + firstName + '\'' +
      ", email='" + email + '\'' +
      ", role=" + role +
      '}';
  }

}
