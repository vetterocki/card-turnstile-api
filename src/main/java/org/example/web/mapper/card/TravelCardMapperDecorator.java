package org.example.web.mapper.card;

import lombok.RequiredArgsConstructor;
import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;
import org.example.model.TravelCard;
import org.example.web.dto.card.DefaultTravelCardModifyDto;
import org.example.web.dto.card.LoyaltyTravelCardModifyDto;
import org.example.web.dto.card.TravelCardModifyDto;
import org.example.web.dto.card.TravelCardViewDto;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TravelCardMapperDecorator {
  private final TravelCardMapper travelCardMapper;

  public TravelCard partialUpdate(TravelCardModifyDto dto, TravelCard target) {
    if (dto instanceof DefaultTravelCardModifyDto dd && target instanceof DefaultTravelCard dtc) {
      return travelCardMapper.partialDefault(dd, dtc);
    } else if (dto instanceof LoyaltyTravelCardModifyDto ld &&
        target instanceof LoyaltyTravelCard ltc) {
      return travelCardMapper.partialLoyalty(ld, ltc);
    } else throw new IllegalArgumentException();
  }

  public TravelCard toEntity(TravelCardModifyDto dto) {
    return travelCardMapper.toEntity(dto);
  }

  public TravelCardViewDto toDto(TravelCard travelCard) {
    return travelCardMapper.toDto(travelCard);
  }
}
