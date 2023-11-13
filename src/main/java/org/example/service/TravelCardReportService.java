package org.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.model.TravelCardReport;
import org.example.model.Turnstile;
import org.example.model.card.TravelCard;
import org.example.model.card.TravelCardType;

public interface TravelCardReportService {
  Optional<TravelCardReport> findByTravelCardAndTurnstile(TravelCard travelCard,
                                                          Turnstile turnstile);

  List<TravelCardReport> findAllByTurnstile(Turnstile turnstile);

  TravelCardReport update(TravelCard travelCard, Turnstile turnstile);

  Map<TravelCardType, List<TravelCardReport>> findAllByTurnstileGroupedByTravelCardType(
      Turnstile turnstile);
}
