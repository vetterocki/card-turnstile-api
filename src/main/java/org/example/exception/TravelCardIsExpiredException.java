package org.example.exception;

public class TravelCardIsExpiredException extends RuntimeException {
  public TravelCardIsExpiredException(Long id) {
    super(String.format("Travel card with id %d is expired.", id));
  }
}
