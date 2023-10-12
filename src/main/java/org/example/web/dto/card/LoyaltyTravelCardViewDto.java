package org.example.web.dto.card;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoyaltyTravelCardViewDto extends TravelCardViewDto {
  private String cardBalance;
}
