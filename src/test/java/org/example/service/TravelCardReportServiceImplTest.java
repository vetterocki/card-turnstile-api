package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.JpaRepositoryUtils.findById;
import static org.example.model.Interaction.InteractionType.DENIED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.assertj.core.api.Condition;
import org.example.data.TravelCardReportRepository;
import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.model.Interaction;
import org.example.model.LoyaltyTravelCard;
import org.example.model.TravelCardReport;
import org.example.model.TravelCardType;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Import(TestContainerConfiguration.class)
@SpringBootTest
class TravelCardReportServiceImplTest {

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
    travelCardRepository.deleteAll();
    turnstileRepository.deleteAll();
  }

  @Test
  @Order(1)
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql", "/sql/reports.sql"})
  void testFindingAllByTurnstileGroupedByTravelCardType() {
    var turnstile = findById(2L, turnstileRepository);

    var groupedBy = travelCardReportService.findAllByTurnstileGroupedByTravelCardType(turnstile);
    assertThat(groupedBy).isNotNull().containsKeys(TravelCardType.SCHOOL, TravelCardType.ORDINARY)
        .hasSize(2);
  }

  @Test
  @Order(2)
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testUpdatingWithSuccessfulInteraction() {
    var travelCard = (LoyaltyTravelCard) findById(4L, travelCardRepository);
    var cardBalanceBefore = travelCard.getCardBalance();
    var turnstile = findById(2L, turnstileRepository);

    assertThat(
        travelCardReportRepository.findByTravelCardAndTurnstile(travelCard, turnstile)).isEmpty();
    var report = travelCardReportService.update(travelCard, turnstile);

    assertAll(
        () -> assertThat(report.getInteractions())
            .isNotEmpty()
            .last()
            .isEqualTo(Interaction.SUCCESS),
        () -> assertThat(cardBalanceBefore)
            .isGreaterThan(
                ((LoyaltyTravelCard) findById(4L, travelCardRepository)).getCardBalance()
            )
    );
  }

  @Test
  @Order(3)
  @Sql({"/sql/turnstiles.sql"})
  void testUpdatingWithDeniedInteraction() {
    var turnstile = findById(2L, turnstileRepository);
    var travelCard = new LoyaltyTravelCard(BigDecimal.ZERO);
    travelCard.setLastPassed(turnstile);
    var created = travelCardRepository.save(travelCard);
    var cardBalanceBefore = created.getCardBalance();
    var report = travelCardReportService.update(travelCard, turnstile);

    assertAll(
        () -> assertThat(report.getInteractions())
            .isNotEmpty()
            .last()
            .matches(interaction -> interaction.getInteractionType().equals(DENIED)),
        () -> assertThat(cardBalanceBefore)
            .isEqualByComparingTo(
                ((LoyaltyTravelCard) findById(created.getId(), travelCardRepository)).getCardBalance()
            )
    );
  }

}
