package org.example.data;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.Turnstile;
import org.example.model.card.DefaultTravelCard;
import org.example.model.card.LoyaltyTravelCard;
import org.example.model.card.TravelAmount;
import org.example.model.card.TravelCard;
import org.example.model.card.TravelCardType;
import org.example.model.card.ValidityPeriod;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@Profile("!test")
public class DatabaseDataLoader {
  private final List<JpaRepository<?, ?>> jpaRepositories;
  private final PasswordEncoder passwordEncoder;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      TravelCard defaultCard =
          new DefaultTravelCard(TravelCardType.SCHOOL, ValidityPeriod.TEN_DAYS, TravelAmount.TEN);
      TravelCard loyaltyCard = new LoyaltyTravelCard(BigDecimal.valueOf(22.4));

      Turnstile turnstile = new Turnstile(BigDecimal.valueOf(7.5));
      defaultCard.setLastPassed(turnstile);
      loyaltyCard.setLastPassed(turnstile);

      repositoryByType(TurnstileRepository.class).save(turnstile);
      repositoryByType(TravelCardRepository.class).saveAll(List.of(defaultCard, loyaltyCard));

      User user = new User();
      user.setEmail("admin@email.com");
      user.setPassword(passwordEncoder.encode("123"));
      user.setRole(Role.ADMIN);
      repositoryByType(UserRepository.class).save(user);
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
