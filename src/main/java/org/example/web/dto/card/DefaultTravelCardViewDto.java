package org.example.web.dto.card;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTravelCardViewDto extends TravelCardViewDto {
  private String expiresAt;
  private int travelsLeft;
}
