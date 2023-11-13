package org.example.service.visitor;

import org.example.model.card.DefaultTravelCard;
import org.example.model.card.LoyaltyTravelCard;

public interface TravelCardVisitor {
  void visitDefault(DefaultTravelCard defaultTravelCard);
  void visitLoyalty(LoyaltyTravelCard loyaltyTravelCard);
}
