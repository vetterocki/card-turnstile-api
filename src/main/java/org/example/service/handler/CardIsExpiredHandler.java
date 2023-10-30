package org.example.service.handler;

import static java.time.LocalDateTime.now;

import org.example.model.DefaultTravelCard;
import org.example.model.Interaction;
import org.example.model.TravelCard;
import org.springframework.stereotype.Component;

@Component
public class CardIsExpiredHandler extends InteractionDescriptionHandler {
  @Override
  public Interaction nodeCheck(TravelCard travelCard) {
    if (travelCard instanceof DefaultTravelCard dtc && dtc.getExpiresAt().isBefore(now())) {
      return Interaction.deniedFrom(String.format("Travel card with id %d is expired.", dtc.getId()));
    }
    return checkNext(travelCard);
  }
}
