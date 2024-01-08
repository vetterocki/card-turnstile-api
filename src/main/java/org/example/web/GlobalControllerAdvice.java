package org.example.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.example.exception.EntityNotFoundException;
import org.example.exception.InvalidPasswordException;
import org.example.exception.InvalidTokenTypeException;
import org.example.exception.UserAlreadyExistsException;
import org.example.web.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleNotFound(RuntimeException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(exceptionResponse(exception.getMessage()));
  }

  @ExceptionHandler({InvalidPasswordException.class, InvalidTokenTypeException.class,
      UserAlreadyExistsException.class})
  public ResponseEntity<ExceptionResponse> handleBadRequest(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exceptionResponse(exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationError(
      MethodArgumentNotValidException exception) {
    var errors = exception.getFieldErrors().stream()
        .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
        .collect(
            Collectors.groupingBy(FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))
        );
    return ResponseEntity.badRequest().body(errors);
  }

  private ExceptionResponse exceptionResponse(String message) {
    return new ExceptionResponse(
        message,
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
    );
  }
}
