package org.example.exception;

public class InvalidTokenTypeException extends RuntimeException {
  public InvalidTokenTypeException(String message) {
    super(message);
  }
}

