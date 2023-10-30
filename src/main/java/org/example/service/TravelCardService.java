package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.TravelCard;

public interface TravelCardService {
  TravelCard save(TravelCard travelCard);

  Optional<TravelCard> findById(Long id);

  List<TravelCard> findAll();
}
