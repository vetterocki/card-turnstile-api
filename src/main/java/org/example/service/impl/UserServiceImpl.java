package org.example.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.data.UserRepository;
import org.example.exception.UserAlreadyExistsException;
import org.example.model.card.TravelCard;
import org.example.model.user.User;
import org.example.service.TravelCardService;
import org.example.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TravelCardService travelCardService;
  private final UserServiceImpl self;

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  public User create(User user) {
    if (!userRepository.existsByEmail(user.getEmail())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    } else {
      throw new UserAlreadyExistsException("User with this phone number already exists!");
    }
  }

  @Override
  @Transactional
  public User update(User user) {
    var userByEmail = findByEmail(user.getEmail())
        .filter(userInDb -> !userInDb.getId().equals(user.getId()));
    if (userByEmail.isPresent()) {
      throw new UserAlreadyExistsException("User with this email already exists");
    }
    return self.create(user);
  }

  @Override
  public Optional<User> findById(Long userId) {
    return userRepository.findById(userId);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public List<TravelCard> findAllTravelCardsByUser(User user) {
    return travelCardService.findAllTravelCardsByUser(user);
  }

}
