package org.example.data;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.DefaultTravelCard;
import org.example.model.LoyaltyTravelCard;
import org.example.model.TravelAmount;
import org.example.model.TravelCard;
import org.example.model.TravelCardType;
import org.example.model.Turnstile;
import org.example.model.ValidityPeriod;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Configuration
public class DatabaseDataLoader {

  private final List<JpaRepository<?, ?>> jpaRepositories;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      TravelCard defaultCard =
          new DefaultTravelCard(TravelCardType.SCHOOL, ValidityPeriod.TEN_DAYS, TravelAmount.TEN);
      TravelCard loyaltyCard = new LoyaltyTravelCard(BigDecimal.valueOf(22.4));

      repositoryByType(TravelCardRepository.class).saveAll(List.of(defaultCard, loyaltyCard));

      Turnstile turnstile = new Turnstile(BigDecimal.valueOf(7.5));

      repositoryByType(TurnstileRepository.class).save(turnstile);
    };
  }

  private <T extends JpaRepository<?, ?>> T repositoryByType(Class<T> repositoryType) {
    return jpaRepositories.stream()
        .filter(repositoryType::isInstance)
        .map(repositoryType::cast)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }


}
