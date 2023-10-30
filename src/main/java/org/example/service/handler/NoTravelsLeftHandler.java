package org.example.service.handler;

import org.example.model.DefaultTravelCard;
import org.example.model.Interaction;
import org.example.model.TravelCard;
import org.springframework.stereotype.Component;


@Component
public class NoTravelsLeftHandler extends InteractionDescriptionHandler {
  @Override
  public Interaction nodeCheck(TravelCard travelCard) {
    if (travelCard instanceof DefaultTravelCard dtc && (dtc.getTravelsLeft() <= 0)) {
      return Interaction.deniedFrom(
          String.format("No travels left on travel card with id %d", dtc.getId())
      );
    }
    return checkNext(travelCard);
  }
}
