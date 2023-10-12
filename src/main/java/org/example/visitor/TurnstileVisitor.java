package org.example.visitor;

import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;

public interface TurnstileVisitor {
  DefaultTravelCard visitDefault(DefaultTravelCard defaultTravelCard);
  LoyaltyTravelCard visitLoyalty(LoyaltyTravelCard loyaltyTravelCard);
}
