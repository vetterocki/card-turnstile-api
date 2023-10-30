package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.JpaRepositoryUtils.findById;
import static org.example.model.Interaction.InteractionType.DENIED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.example.data.TravelCardReportRepository;
import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.model.DefaultTravelCard;
import org.example.model.Interaction;
import org.example.model.LoyaltyTravelCard;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Import({TestContainerConfiguration.class})
@SpringBootTest
class InteractionGeneratorTest {
  @Autowired
  private TravelCardReportRepository travelCardReportRepository;

  @Autowired
  private TravelCardRepository travelCardRepository;

  @Autowired
  private TurnstileRepository turnstileRepository;

  @Autowired
  private InteractionGenerator interactionGenerator;

  @AfterEach
  void clearAll() {
    travelCardRepository.deleteAll();
    turnstileRepository.deleteAll();
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testGenerationForValidTravelCards() {
    var dtc = (DefaultTravelCard) findById(3L, travelCardRepository);
    var ltc = (LoyaltyTravelCard) findById(4L, travelCardRepository);
    assertAll("Valid state in both travel cards", Stream.of(dtc, ltc)
        .map(interactionGenerator::generate)
        .map(Interaction::isSuccessful)
        .map(condition -> (Executable) () -> assertTrue(condition))
        .toList());
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testGenerationForInvalidLoyaltyTravelCard() {
    var ltc = (LoyaltyTravelCard) findById(4L, travelCardRepository);
    ltc.setCardBalance(BigDecimal.ZERO);
    var interaction = interactionGenerator.generate(ltc);
    assertEquals(DENIED, interaction.getInteractionType());
    assertThat(interaction.getDescription())
        .doesNotContain("null")
        .contains("Not enough funds to pay");
  }
}
