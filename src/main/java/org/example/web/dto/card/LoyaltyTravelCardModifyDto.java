package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonTypeName("loyalty")
@EqualsAndHashCode(callSuper = true)
@Data
public class LoyaltyTravelCardModifyDto extends TravelCardModifyDto {
  @NotNull(message = "Specify new card balance")
  @Positive(message = "Card balance must be positive")
  private BigDecimal cardBalance;
}
