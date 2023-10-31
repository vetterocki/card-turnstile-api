package org.example.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;


@Data
public class TurnstileModifyDto {
  @NotNull(message = "Specify turnstile fare price")
  @Positive(message = "Price must be positive")
  protected BigDecimal farePrice;
}
