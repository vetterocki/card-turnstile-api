package org.example.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.data.TurnstileRepository;
import org.example.exception.EntityNotFoundException;
import org.example.model.Interaction;
import org.example.model.TravelCard;
import org.example.model.TravelCardReport;
import org.example.model.TravelCardType;
import org.example.model.Turnstile;
import org.example.service.TravelCardReportService;
import org.example.service.TravelCardService;
import org.example.service.TurnstileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TurnstileServiceImpl implements TurnstileService {
  private final TurnstileRepository turnstileRepository;
  private final TravelCardService travelCardService;
  private final TravelCardReportService travelCardReportService;

  @Override
  public Turnstile create(Turnstile turnstile) {
    return turnstileRepository.save(turnstile);
  }

  @Override
  @Transactional
  public Interaction passTurnstile(Long travelCardId, Long turnstileId) {
    return findById(turnstileId).map(
            turnstile -> travelCardService.findById(travelCardId).map(travelCard -> {
              travelCard.setLastPassed(turnstile);
              var report = travelCardReportService.update(travelCard, turnstile);
              return report.getInteractions().get(report.getInteractions().size() - 1);
            }).orElseThrow(() -> new EntityNotFoundException(travelCardId, TravelCard.class)))
        .orElseThrow(() -> new EntityNotFoundException(turnstileId, Turnstile.class));
  }

  @Override
  public Optional<Turnstile> findById(Long id) {
    return turnstileRepository.findById(id);
  }

  @Override
  public Optional<List<TravelCardReport>> findAllReports(Long id) {
    return findById(id).map(travelCardReportService::findAllByTurnstile);
  }

  @Override
  public Optional<Map<TravelCardType, List<TravelCardReport>>> findAllReportsGroupedByTravelCardType(
      Long id) {
    return findById(id).map(travelCardReportService::findAllByTurnstileGroupedByTravelCardType);
  }

  @Override
  public void deleteById(Long id) {
    turnstileRepository.deleteById(id);
  }
}
