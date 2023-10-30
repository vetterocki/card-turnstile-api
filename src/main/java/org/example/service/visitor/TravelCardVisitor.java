package org.example.service.visitor;

import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;

public interface TravelCardVisitor {
  void visitDefault(DefaultTravelCard defaultTravelCard);
  void visitLoyalty(LoyaltyTravelCard loyaltyTravelCard);
}
