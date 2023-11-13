package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.model.card.DefaultTravelCard;
import org.example.model.card.TravelAmount;
import org.example.model.card.TravelCard;
import org.example.model.card.TravelCardType;
import org.example.model.card.ValidityPeriod;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({TestContainerConfiguration.class})
@SpringBootTest
class TravelCardServiceTest {
  @Autowired
  private TravelCardRepository travelCardRepository;

  @Autowired
  private TurnstileRepository turnstileRepository;

  @Autowired
  private TravelCardService travelCardService;

  @AfterEach
  public void deleteAll() {
    travelCardRepository.deleteAll();
    turnstileRepository.deleteAll();
  }

  @Test
  void testCreation() {
    TravelCard travelCard =
        new DefaultTravelCard(TravelCardType.ORDINARY, ValidityPeriod.TEN_DAYS, TravelAmount.TEN);
    var created = travelCardService.save(travelCard);
    assertThat(created.getId()).isNotNull().isPositive();
  }
}
