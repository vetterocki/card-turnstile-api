package org.example.exception;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(Long id) {
    super(String.format("Could not find entity by id %d", id));
  }

  public EntityNotFoundException(Long id, Class<?> clazz) {
    super(String.format("Could not find %s by id %d", clazz.getSimpleName(), id));
  }
}
