package fr.cheron.antoine.hypermedia.noannotation.exceptions;

public class NotFoundResourceException extends Exception {

  public NotFoundResourceException(String resourceName) {
    super("Resource " + resourceName + " not found.");
  }

}
