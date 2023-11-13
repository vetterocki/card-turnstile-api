package org.example.service;

import org.example.model.Interaction;
import org.example.model.card.TravelCard;

public interface InteractionGenerator {
  Interaction generate(TravelCard travelCard);
}
