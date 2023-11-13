package org.example.web.dto.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.model.card.TravelCardType;


@JsonTypeName("loyalty")
@EqualsAndHashCode(callSuper = true)
@Getter
public class LoyaltyTravelCardModifyDto extends TravelCardModifyDto {
  @Setter
  @NotNull(message = "Specify new card balance")
  @Positive(message = "Card balance must be positive")
  private BigDecimal cardBalance;

  private final TravelCardType travelCardType = TravelCardType.ORDINARY;
}
