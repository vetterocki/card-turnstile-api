package org.example.service.visitor;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.example.model.card.DefaultTravelCard;
import org.example.model.card.LoyaltyTravelCard;
import org.example.model.card.TravelCard;
import org.example.service.TravelCardService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Component
public class TurnstilePassTravelCardVisitor implements TravelCardVisitor {
  private final TravelCardService travelCardService;
  private final TurnstilePassTravelCardVisitor self;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void visitDefault(DefaultTravelCard travelCard) {
    self.ifExists(travelCard, inDb -> {
      travelCard.setTravelsLeft(travelCard.getTravelsLeft() - 1);
      travelCardService.save(travelCard);
    });
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void visitLoyalty(LoyaltyTravelCard travelCard) {
    self.ifExists(travelCard, inDb -> {
      var cardBalance = travelCard.getCardBalance();
      var price = travelCard.getLastPassed().getFarePrice();
      travelCard.setCardBalance(cardBalance.subtract(price));
      travelCardService.save(travelCard);
    });
  }

  @Transactional
  public void ifExists(TravelCard travelCard, Consumer<TravelCard> action) {
    travelCardService.findById(travelCard.getId()).ifPresent(action.andThen(travelCardService::save));
  }
}
