package org.example.exception;

public class NoTravelsLeftException extends RuntimeException {
  public NoTravelsLeftException(Long id) {
    super(String.format("No travels left on travel card with id %d", id));
  }
}
