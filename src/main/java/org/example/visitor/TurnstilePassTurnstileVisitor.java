package org.example.visitor;

import lombok.RequiredArgsConstructor;
import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;
import org.example.service.validator.EntityValidator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TurnstilePassTurnstileVisitor implements TurnstileVisitor {
  private final EntityValidator<DefaultTravelCard> defaultTravelCardValidator;
  private final EntityValidator<LoyaltyTravelCard> loyaltyTravelCardValidator;

  @Override
  public DefaultTravelCard visitDefault(DefaultTravelCard travelCard) {
    defaultTravelCardValidator.validate(travelCard);
    travelCard.setTravelsLeft(travelCard.getTravelsLeft() - 1);
    return travelCard;
  }

  @Override
  public LoyaltyTravelCard visitLoyalty(LoyaltyTravelCard travelCard) {
    loyaltyTravelCardValidator.validate(travelCard);
    var cardBalance = travelCard.getCardBalance();
    var price = travelCard.getLastPassed().getFarePrice();
    travelCard.setCardBalance(cardBalance.subtract(price));
    return travelCard;
  }
}
