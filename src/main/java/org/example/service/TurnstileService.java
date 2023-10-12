package org.example.service;

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
import org.example.visitor.TurnstileVisitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TurnstileService {
  private final TurnstileRepository turnstileRepository;
  private final TravelCardService travelCardService;
  private final TravelCardReportService travelCardReportService;
  private final TurnstileVisitor turnstileVisitor;

  public Turnstile create(Turnstile turnstile) {
    return turnstileRepository.save(turnstile);
  }

  @Transactional
  public Interaction passTurnstile(Long travelCardId, Long turnstileId) {
    return findById(turnstileId)
        .map(turnstile -> travelCardService.findById(travelCardId).map(travelCard -> {
          travelCard.setLastPassed(turnstile);
          var report = travelCardReportService.findByTravelCardAndTurnstile(travelCard, turnstile);
          Interaction interaction;
          try {
            travelCard.accept(turnstileVisitor);
            interaction = Interaction.SUCCESS;
          } catch (RuntimeException exception) {
            interaction = Interaction.deniedFrom(exception.getMessage());
          }
          travelCardReportService.update(report);
          report.addInteraction(interaction);
          return interaction;
        }).orElseThrow(() -> new EntityNotFoundException(travelCardId, TravelCard.class)))
        .orElseThrow(() -> new EntityNotFoundException(turnstileId, Turnstile.class));
  }

  public Optional<Turnstile> findById(Long id) {
    return turnstileRepository.findById(id);
  }

  public Optional<List<TravelCardReport>> findAllReports(Long id) {
    return findById(id).map(travelCardReportService::findAllByTurnstile);
  }

  public Optional<Map<TravelCardType, List<TravelCardReport>>>
  findAllReportsGroupedByTravelCardType(Long id) {
    return findById(id).map(travelCardReportService::findAllByTurnstileGroupedByTravelCardType);
  }

  public void deleteById(Long id) {
    turnstileRepository.deleteById(id);
  }
}
