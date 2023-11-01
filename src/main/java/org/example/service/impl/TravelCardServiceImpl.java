package org.example.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.data.TravelCardRepository;
import org.example.model.TravelCard;
import org.example.service.TravelCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelCardServiceImpl implements TravelCardService {
  private final TravelCardRepository travelCardRepository;

  @Override
  @Transactional
  public TravelCard save(TravelCard travelCard) {
    return travelCardRepository.save(travelCard);
  }

  @Override
  public Optional<TravelCard> findById(Long id) {
    return travelCardRepository.findById(id);
  }

  @Override
  public List<TravelCard> findAll() {
    return travelCardRepository.findAll();
  }

  @Override
  public void deleteById(Long id) {
    travelCardRepository.deleteById(id);
  }

}
