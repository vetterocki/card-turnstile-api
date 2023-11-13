package org.example.web.dto.card;

import lombok.Data;
import org.example.model.card.TravelCardType;

@Data
public abstract class TravelCardViewDto  {
  private Long id;
  private Long lastPassedId;
  private TravelCardType travelCardType;
}
