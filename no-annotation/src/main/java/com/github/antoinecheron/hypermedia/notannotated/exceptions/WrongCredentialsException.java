package com.github.antoinecheron.hypermedia.notannotated.exceptions;

public class WrongCredentialsException extends BadRequestException {

  public WrongCredentialsException() {
    super("Incorrect username or password");
  }

}
