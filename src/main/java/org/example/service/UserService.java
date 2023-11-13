package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.card.TravelCard;
import org.example.model.user.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
  @Transactional(propagation = Propagation.MANDATORY)
  User create(User user);

  @Transactional
  User update(User user);

  Optional<User> findById(Long userId);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  List<User> findAll();

  void deleteById(Long id);

  List<TravelCard> findAllTravelCardsByUser(User user);
}
