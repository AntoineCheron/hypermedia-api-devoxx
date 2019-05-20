package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.Objects;

public class UserCreationFormWithoutRole {

  private final String lastName;
  private final String firstName;
  private final String email;
  private final String password;
  private final String passwordConfirm;

  public UserCreationFormWithoutRole(String lastName, String firstName, String email, String password, String passwordConfirm) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.password = password;
    this.passwordConfirm = passwordConfirm;
  }

  public UserCreationFormWithRole withRole(UserRole role) {
    return new UserCreationFormWithRole(lastName, firstName, email, password, passwordConfirm, role);
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

  public String getPassword() {
    return password;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCreationFormWithoutRole that = (UserCreationFormWithoutRole) o;
    return Objects.equals(lastName, that.lastName) &&
      Objects.equals(firstName, that.firstName) &&
      Objects.equals(email, that.email) &&
      Objects.equals(passwordConfirm, that.passwordConfirm) &&
      Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastName, firstName, email, password);
  }

  @Override
  public String toString() {
    return "UserCreationFormWithoutRole{" +
      "lastName='" + lastName + '\'' +
      ", firstName='" + firstName + '\'' +
      ", email='" + email + '\'' +
      ", password='" + password + '\'' +
      ", passwordConfirm='" + passwordConfirm + '\'' +
      '}';
  }

}
