package com.github.antoinecheron.hypermedia.notannotated.security;

import java.util.Objects;

public class JsonWebToken {

  private final String token;

  public JsonWebToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JsonWebToken that = (JsonWebToken) o;
    return Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return "JsonWebToken{" +
      "token='" + token + '\'' +
      '}';
  }

}
