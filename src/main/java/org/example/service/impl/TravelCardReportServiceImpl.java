package org.example.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.data.TravelCardReportRepository;
import org.example.model.Interaction;
import org.example.model.TravelCardReport;
import org.example.model.Turnstile;
import org.example.model.card.TravelCard;
import org.example.model.card.TravelCardType;
import org.example.service.TravelCardReportService;
import org.example.service.visitor.TravelCardVisitor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TravelCardReportServiceImpl implements TravelCardReportService {
  private final TravelCardReportRepository travelCardReportRepository;
  private final HandlerChainInteractionGenerator handlerChainInteractionGenerator;
  private final TravelCardVisitor travelCardVisitor;

  @Override
  public Optional<TravelCardReport> findByTravelCardAndTurnstile(TravelCard travelCard,
                                                                 Turnstile turnstile) {
    return travelCardReportRepository.findByTravelCardAndTurnstile(travelCard, turnstile);
  }

  @Override
  public List<TravelCardReport> findAllByTurnstile(Turnstile turnstile) {
    return travelCardReportRepository.findAllByTurnstile(turnstile);
  }

  @Override
  public TravelCardReport update(TravelCard travelCard, Turnstile turnstile) {
    var travelCardReport = findByTravelCardAndTurnstile(travelCard, turnstile)
        .orElseGet(() -> new TravelCardReport(travelCard, turnstile));
    Interaction interaction = handlerChainInteractionGenerator.generate(travelCard);
    if (interaction.isSuccessful()) {
      travelCard.accept(travelCardVisitor);
    }
    travelCardReport.addInteraction(interaction);
    return travelCardReportRepository.save(travelCardReport);
  }

  @Override
  public Map<TravelCardType, List<TravelCardReport>> findAllByTurnstileGroupedByTravelCardType(
      Turnstile turnstile) {
    return findAllByTurnstile(turnstile).stream()
        .collect(Collectors.groupingBy(
            travelCardReport -> travelCardReport.getTravelCard().getTravelCardType(),
            TreeMap::new,
            Collectors.toList())
        );
  }
}
