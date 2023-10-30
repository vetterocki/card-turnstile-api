package org.example.service.handler;

import java.math.BigDecimal;
import org.example.model.Interaction;
import org.example.model.LoyaltyTravelCard;
import org.example.model.TravelCard;
import org.springframework.stereotype.Component;

@Component
public class InsufficientFundsHandler extends InteractionDescriptionHandler {
  @Override
  public Interaction nodeCheck(TravelCard travelCard) {
    if (travelCard instanceof LoyaltyTravelCard loyaltyTravelCard) {
      var price = loyaltyTravelCard.getLastPassed().getFarePrice();
      var cardBalance = loyaltyTravelCard.getCardBalance();
      if (cardBalance.subtract(price).compareTo(BigDecimal.ZERO) < 0) {
        return Interaction.deniedFrom(
            String.format("Not enough funds to pay %s, current balance: %s", price, cardBalance));
      }
    }
    return checkNext(travelCard);
  }

}
