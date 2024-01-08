package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.card.TravelCard;
import org.example.model.user.User;

public interface UserService {
  User create(User user);

  User update(User user);

  Optional<User> findById(Long userId);

  Optional<User> findByEmail(String email);

  void deleteById(Long id);

  List<TravelCard> findAllTravelCardsByUser(User user);
}
