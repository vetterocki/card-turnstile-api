package org.example.service.handler;

import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.model.Interaction;
import org.example.model.card.TravelCard;

@EqualsAndHashCode
@ToString
@Getter
public abstract class InteractionDescriptionHandler {
  protected InteractionDescriptionHandler next;

  public abstract Interaction nodeCheck(TravelCard travelCard);

  public static InteractionDescriptionHandler chain(InteractionDescriptionHandler first,
                                                    List<InteractionDescriptionHandler> nodes) {
    InteractionDescriptionHandler head = first;
    for (var node : nodes) {
      head.next = node;
      head = node;
    }
    return first;
  }

  protected Interaction checkNext(TravelCard travelCard) {
    return Optional.ofNullable(next)
        .map(nextNode -> nextNode.nodeCheck(travelCard))
        .orElse(Interaction.SUCCESS);
  }
}

