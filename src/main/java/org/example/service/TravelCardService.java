package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.card.TravelCard;
import org.example.model.user.User;

public interface TravelCardService {
  TravelCard save(TravelCard travelCard);

  Optional<TravelCard> findById(Long id);

  List<TravelCard> findAll();

  void deleteById(Long id);

  List<TravelCard> findAllTravelCardsByUser(User user);
}
