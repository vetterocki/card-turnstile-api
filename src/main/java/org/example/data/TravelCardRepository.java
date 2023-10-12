package org.example.data;

import java.util.Optional;
import org.example.model.TravelCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelCardRepository extends JpaRepository<TravelCard, Long> {
  <T extends TravelCard> Optional<T> findTravelCardById(Long id);

}
