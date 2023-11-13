package org.example.data;

import java.util.List;
import org.example.model.card.TravelCard;
import org.example.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelCardRepository extends JpaRepository<TravelCard, Long> {
  List<TravelCard> findAllByUser(User user);
}
