package org.example.service.validator;

import java.math.BigDecimal;
import org.example.exception.InsufficientFundsOnCardBalanceException;
import org.example.model.LoyaltyTravelCard;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyTravelCardValidator implements EntityValidator<LoyaltyTravelCard> {
  @Override
  public void validate(LoyaltyTravelCard entity) {
    var price = entity.getLastPassed().getFarePrice();
    var cardBalance = entity.getCardBalance();
    if (cardBalance.subtract(price).compareTo(BigDecimal.ZERO) < 0) {
      throw new InsufficientFundsOnCardBalanceException(cardBalance, price);
    }
  }
}
