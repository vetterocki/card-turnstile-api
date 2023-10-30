package org.example.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.Interaction;
import org.example.model.TravelCard;
import org.example.service.InteractionGenerator;
import org.example.service.handler.InteractionDescriptionHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandlerChainInteractionGenerator implements InteractionGenerator {
  private final List<InteractionDescriptionHandler> interactionHandlers;

  @Override
  public Interaction generate(TravelCard travelCard) {
    InteractionDescriptionHandler interactionHandlersChain = InteractionDescriptionHandler.chain(
        interactionHandlers.get(0), interactionHandlers.subList(1, interactionHandlers.size())
    );
    return interactionHandlersChain.nodeCheck(travelCard);
  }
}
