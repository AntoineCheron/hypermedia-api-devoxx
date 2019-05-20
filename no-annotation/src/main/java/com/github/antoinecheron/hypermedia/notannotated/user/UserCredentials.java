package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.Objects;

public class UserCredentials {

  private final String userId;
  private final String password;

  public UserCredentials(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }

  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCredentials that = (UserCredentials) o;
    return Objects.equals(userId, that.userId) &&
      Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, password);
  }

  @Override
  public String toString() {
    return "UserCredentials{" +
      "userId='" + userId + '\'' +
      ", password='" + password + '\'' +
      '}';
  }

}
