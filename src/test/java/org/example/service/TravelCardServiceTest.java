package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.data.TravelCardRepository;
import org.example.model.DefaultTravelCard;
import org.example.model.TravelAmount;
import org.example.model.TravelCard;
import org.example.model.TravelCardType;
import org.example.model.ValidityPeriod;
import org.example.testcontainer.TestContainerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
  private TravelCardService travelCardService;

  @AfterEach
  public void deleteAll() {
    travelCardRepository.deleteAll();
  }

  @Test
  void testCreation() {
    TravelCard travelCard =
        new DefaultTravelCard(TravelCardType.ORDINARY, ValidityPeriod.TEN_DAYS, TravelAmount.TEN);
    var created = travelCardService.create(travelCard);
    assertThat(created.getId()).isNotNull().isPositive();
  }

}
