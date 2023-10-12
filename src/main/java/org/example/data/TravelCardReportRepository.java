package org.example.data;

import java.util.List;
import java.util.Optional;
import org.example.model.TravelCard;
import org.example.model.TravelCardReport;
import org.example.model.Turnstile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelCardReportRepository extends JpaRepository<TravelCardReport, Long> {
  Optional<TravelCardReport> findByTravelCardAndTurnstile(TravelCard travelCard,
                                                          Turnstile turnstile);

  List<TravelCardReport> findAllByTurnstile(Turnstile turnstile);
}
