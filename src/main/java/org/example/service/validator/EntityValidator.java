package org.example.service.validator;

public interface EntityValidator<T> {
  void validate(T entity);
}
