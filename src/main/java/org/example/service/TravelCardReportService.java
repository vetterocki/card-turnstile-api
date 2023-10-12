package org.example.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.data.TravelCardReportRepository;
import org.example.model.TravelCard;
import org.example.model.TravelCardReport;
import org.example.model.TravelCardType;
import org.example.model.Turnstile;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TravelCardReportService {
  private final TravelCardReportRepository travelCardReportRepository;

  public TravelCardReport findByTravelCardAndTurnstile(TravelCard travelCard, Turnstile turnstile) {
    return travelCardReportRepository.findByTravelCardAndTurnstile(travelCard, turnstile)
        .orElseGet(() -> new TravelCardReport(travelCard, turnstile));
  }

  public List<TravelCardReport> findAllByTurnstile(Turnstile turnstile) {
    return travelCardReportRepository.findAllByTurnstile(turnstile);
  }

  public TravelCardReport update(TravelCardReport travelCardReport) {
    return travelCardReportRepository.save(travelCardReport);
  }

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
