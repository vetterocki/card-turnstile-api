package org.example.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.JpaRepositoryUtils.findById;

import org.example.data.TravelCardReportRepository;
import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.exception.EntityNotFoundException;
import org.example.model.DefaultTravelCard;
import org.example.model.Interaction;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Import({TestContainerConfiguration.class})
@SpringBootTest
class TurnstileServiceTest {
  @Autowired
  private TurnstileService turnstileService;

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
  void testTurnstilePassing() {
    var travelCard = (DefaultTravelCard) findById(3L, travelCardRepository);
    var turnstile = findById(2L, turnstileRepository);

    var successfulInteraction = turnstileService.passTurnstile(3L, 2L);

    assertThat(successfulInteraction).isEqualTo(Interaction.SUCCESS);
    assertThat(travelCardReportRepository.findByTravelCardAndTurnstile(travelCard, turnstile))
        .isNotEmpty()
        .hasValueSatisfying(travelCardReport -> {
          assertThat(travelCardReport.getTurnstile()).isEqualTo(turnstile);
          assertThat(travelCardReport.getTravelCard()).isEqualTo(travelCard);
        });

    travelCard.setTravelsLeft(0);
    travelCardRepository.save(travelCard);

    var deniedInteraction = turnstileService.passTurnstile(3L, 2L);
    assertThat(deniedInteraction).satisfies(interaction -> {
      assertThat(interaction.getDescription()).isEqualTo(
          "No travels left on travel card with id " + travelCard.getId());
    });

    assertThatThrownBy(() -> turnstileService.passTurnstile(10L, 10L))
        .isInstanceOf(EntityNotFoundException.class);

  }
}
