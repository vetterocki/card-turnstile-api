package org.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.model.Interaction;
import org.example.model.TravelCardReport;
import org.example.model.Turnstile;
import org.example.model.card.TravelCardType;

public interface TurnstileService {
  Turnstile create(Turnstile turnstile);

  Interaction passTurnstile(Long travelCardId, Long turnstileId);

  Optional<Turnstile> findById(Long id);

  Optional<List<TravelCardReport>> findAllReports(Long id);

  Optional<Map<TravelCardType, List<TravelCardReport>>> findAllReportsGroupedByTravelCardType(
      Long id);

  void deleteById(Long id);
}
