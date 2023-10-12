package org.example.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.data.TravelCardRepository;
import org.example.model.TravelCard;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TravelCardService {
  private final TravelCardRepository travelCardRepository;

  public TravelCard create(TravelCard travelCard) {
    return travelCardRepository.save(travelCard);
  }

  public Optional<TravelCard> findById(Long id) {
    return travelCardRepository.findTravelCardById(id);
  }

  public List<TravelCard> findAll() {
    return travelCardRepository.findAll();
  }

}
