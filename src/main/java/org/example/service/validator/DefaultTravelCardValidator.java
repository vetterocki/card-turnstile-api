package org.example.service.validator;

import java.time.LocalDateTime;
import org.example.exception.NoTravelsLeftException;
import org.example.exception.TravelCardIsExpiredException;
import org.example.model.DefaultTravelCard;
import org.springframework.stereotype.Component;

@Component
public class DefaultTravelCardValidator implements EntityValidator<DefaultTravelCard> {
  @Override
  public void validate(DefaultTravelCard entity) {
    if (entity.getTravelsLeft() == 0) {
      throw new NoTravelsLeftException(entity.getId());
    }
    if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new TravelCardIsExpiredException(entity.getId());
    }
  }
}
