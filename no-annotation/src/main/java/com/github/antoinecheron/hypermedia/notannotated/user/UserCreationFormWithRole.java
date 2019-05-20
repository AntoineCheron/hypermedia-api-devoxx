package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.Objects;

public class UserCreationFormWithRole extends UserCreationFormWithoutRole {

  private final UserRole role;

  public UserCreationFormWithRole(String lastName, String firstName, String email, String password, String passwordConfirm, UserRole role) {
    super(lastName, firstName, email, password, passwordConfirm);
    this.role = role;
  }

  public UserRole getRole() {
    return role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    UserCreationFormWithRole that = (UserCreationFormWithRole) o;
    return role == that.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), role);
  }

  @Override
  public String toString() {
    return "UserCreationFormWithRole{" +
      "role=" + role +
      '}';
  }

}
