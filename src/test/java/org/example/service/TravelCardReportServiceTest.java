package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.JpaRepositoryUtils.findById;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.example.data.TravelCardReportRepository;
import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.model.TravelCardReport;
import org.example.model.TravelCardType;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Import(TestContainerConfiguration.class)
@SpringBootTest
class TravelCardReportServiceTest {

  @Autowired
  private TravelCardReportService travelCardReportService;

  @Autowired
  private TravelCardReportRepository travelCardReportRepository;

  @Autowired
  private TravelCardRepository travelCardRepository;

  @Autowired
  private TurnstileRepository turnstileRepository;

  @AfterEach
  void clearAll() {
    travelCardReportRepository.deleteAll();
    travelCardRepository.deleteAll();
    turnstileRepository.deleteAll();
  }

  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  @Test
  void testFindingByTravelCardAndTurnstile() {
    Long reportId = 1L;
    var travelCard = findById(3L, travelCardRepository);
    var turnstile = findById(2L, turnstileRepository);

    var expected = new TravelCardReport(travelCard, turnstile);

    assertFalse(travelCardReportRepository.existsById(reportId));
    assertThat(travelCardReportService.findByTravelCardAndTurnstile(travelCard, turnstile))
        .isNotNull()
        .isInstanceOf(TravelCardReport.class)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql", "/sql/reports.sql"})
  @Test
  void testFindingAllByTurnstileGroupedByTravelCardType() {
    var turnstile = findById(2L, turnstileRepository);

    var groupedBy = travelCardReportService.findAllByTurnstileGroupedByTravelCardType(turnstile);
    assertThat(groupedBy)
        .isNotNull()
        .containsKeys(TravelCardType.SCHOOL, TravelCardType.ORDINARY)
        .hasSize(2);
  }
}
