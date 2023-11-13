package org.example.service;

import java.util.Optional;
import org.example.model.user.Token;
import org.example.model.user.User;
import org.springframework.data.util.Pair;

public interface TokenService {
  Pair<Token, Token> create(User user);

  void invalidateAllUserTokens(Long userId);

  Optional<Token> findByTokenValue(String token);
}
